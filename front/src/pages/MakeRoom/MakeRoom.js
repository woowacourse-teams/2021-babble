import './MakeRoom.scss';

import { Body2, Headline2 } from '../../core/Typography';
import {
  DropdownInput,
  MainImage,
  ModalError,
  RoundButton,
  SearchInput,
} from '../../components';
import { Link, useHistory, useLocation } from 'react-router-dom';
import React, { useEffect, useState } from 'react';

import { BASE_URL } from '../../constants/api';
import ChattingRoom from '../ChattingRoom/ChattingRoom';
import PATH from '../../constants/path';
import { PATTERNS } from '../../constants/regex';
import PageLayout from '../../core/Layout/PageLayout';
import PropTypes from 'prop-types';
import TagList from '../../chunks/TagList/TagList';
import axios from 'axios';
import getKorRegExp from '../../components/SearchInput/service/getKorRegExp';
import { useChattingModal } from '../../contexts/ChattingModalProvider';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const MakeRoom = ({ match }) => {
  const location = useLocation();
  const history = useHistory();
  const [imageUrl, setImageUrl] = useState('');
  const [tagList, setTagList] = useState([]);
  const [selectedTagList, setSelectedTagList] = useState([]);
  const [maxHeadCount, setMaxHeadCount] = useState(0);
  const { openChatting, closeChatting } = useChattingModal();
  const { openModal } = useDefaultModal();

  // TODO: 임시 방편. onChangeInput을 SearchInput 내부로 집어넣으면서 사라질 운명
  const [autoCompleteTagList, setAutoCompleteTagList] = useState([]);

  const { gameId } = match.params;

  // TODO: Modal로 바꾼 후 사라질 운명
  if (!location.state) {
    history.replace(`/games/${gameId}`);
    return null;
  }

  const { gameName } = location.state;

  const getImage = async () => {
    try {
      const response = await axios.get(
        `${BASE_URL}/api/games/${gameId}/images`
      );
      const image = response.data.images[0];

      setImageUrl(image);
    } catch (error) {
      openModal(<ModalError>{error.response?.data?.message}</ModalError>);
    }
  };

  const getTags = async () => {
    try {
      const response = await axios.get(`${BASE_URL}/api/tags`);
      const tags = response.data;

      setTagList(tags);
      setAutoCompleteTagList(tags);
    } catch (error) {
      openModal(<ModalError>{error.response?.data?.message}</ModalError>);
    }
  };

  const selectTag = (tagName) => {
    const tagId = tagList.find((tag) => tag.name === tagName).id;
    const tag = { id: tagId, name: tagName };

    setSelectedTagList((prevTagList) =>
      prevTagList.find((prevTag) => prevTag.name === tag.name)
        ? prevTagList
        : [...prevTagList, tag]
    );
  };

  const eraseTag = (e) => {
    // TODO: selectedTag 찾는 로직 고민해보기(element 구조에 종속적임)
    const selectedTagName = e.target
      .closest('.tag-container')
      .querySelector('.caption1').textContent;

    setSelectedTagList((prevTagList) =>
      prevTagList.filter((tag) => tag.name !== selectedTagName)
    );
  };

  // TODO: onChangeTagInput을 SearchInput 내부로 집어넣으면서 사라질 운명.
  const onChangeTagInput = (e) => {
    const inputValue = e.target.value;

    const searchResults = PATTERNS.KOREAN.test(inputValue)
      ? tagList.filter((tag) => {
          const keywordRegExp = getKorRegExp(inputValue, {
            initialSearch: true,
            ignoreSpace: true,
          });
          return tag.name.match(keywordRegExp);
        })
      : tagList.filter((tag) => {
          const searchRegex = new RegExp(inputValue, 'gi');
          const keywordWithoutSpace = tag.name.replace(PATTERNS.SPACE, '');
          return (
            keywordWithoutSpace.match(searchRegex) ||
            tag.name.match(searchRegex)
          );
        });

    setAutoCompleteTagList(searchResults);
  };

  const createRoom = async (e) => {
    e.preventDefault();

    try {
      // TODO: 테스트 서버에서 실제 배포 서버로 변경하기
      const tagIds = selectedTagList.map(({ id }) => ({ id }));
      const response = await axios.post(`${BASE_URL}/api/rooms`, {
        gameId,
        maxHeadCount,
        tags: tagIds,
      });
      const { tags, game, roomId } = response.data;

      closeChatting();
      openChatting(<ChattingRoom tags={tags} game={game} roomId={roomId} />);

      history.push({
        pathname: `${PATH.ROOM_LIST}/${gameId}`,
        state: { gameName },
      });
    } catch (error) {
      openModal(<ModalError>{error.response?.data?.message}</ModalError>);
    }
  };

  const selectMaxHeadCount = (count) => {
    setMaxHeadCount(Number(count));
  };

  useEffect(() => {
    getImage();
    getTags();
  }, []);

  return (
    <div className='make-room-container'>
      <form className='make-room-form' onSubmit={createRoom}>
        <MainImage imageSrc={imageUrl} />
        <PageLayout type='narrow'>
          <Headline2>방 생성하기</Headline2>
          <section className='inputs'>
            {/* TODO: DropdownInput 컴포넌트에서 도메인 제거하기 */}
            <DropdownInput
              dropdownKeywords={[...Array(21).keys()].slice(2)}
              inputValue={maxHeadCount}
              setInputValue={selectMaxHeadCount}
            />
            <SearchInput
              autoCompleteKeywords={autoCompleteTagList}
              onClickKeyword={selectTag}
              onChangeInput={onChangeTagInput}
            />
          </section>
          <section className='tag-list-section'>
            <TagList tags={selectedTagList} onDeleteTag={eraseTag} erasable />
          </section>
          <section className='buttons'>
            <Link to={`${PATH.ROOM_LIST}/${gameId}`}>
              <RoundButton size='small'>
                <Body2>취소하기</Body2>
              </RoundButton>
            </Link>
            <RoundButton type='submit' size='small' colored>
              <Body2>생성하기</Body2>
            </RoundButton>
          </section>
        </PageLayout>
      </form>
    </div>
  );
};

MakeRoom.propTypes = {
  gameId: PropTypes.number,
  match: PropTypes.object,
};

export default MakeRoom;
