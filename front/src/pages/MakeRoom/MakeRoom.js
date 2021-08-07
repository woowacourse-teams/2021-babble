import './MakeRoom.scss';

import { Body2, Headline2 } from '../../core/Typography';
import {
  DropdownInput,
  MainImage,
  RoundButton,
  SearchInput,
} from '../../components';
import { Link, useHistory, useLocation } from 'react-router-dom';
import React, { useEffect, useState } from 'react';

import ChattingRoom from '../ChattingRoom/ChattingRoom';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import PropTypes from 'prop-types';
import TagList from '../../chunks/TagList/TagList';
import axios from 'axios';
import getKorRegExp from '../../components/SearchInput/service/getKorRegExp';
import { useChattingModal } from '../../contexts/ChattingModalProvider';

const MakeRoom = ({ match }) => {
  const location = useLocation();
  const history = useHistory();
  const [imageUrl, setImageUrl] = useState('');
  const [tagList, setTagList] = useState([]);
  const [selectedTagList, setSelectedTagList] = useState([]);
  const [maxHeadCount, setMaxHeadCount] = useState(0);
  const { openChatting, closeChatting } = useChattingModal();

  // TODO: 임시 방편. onChangeInput을 SearchInput 내부로 집어넣으면서 사라질 운명
  const [autoCompleteTagList, setAutoCompleteTagList] = useState([]);

  const { gameId } = match.params;
  const { gameName } = location.state;

  const getImage = async () => {
    const response = await axios.get(
      `https://test-api.babble.gg/api/games/${gameId}/images`
    );
    const image = response.data.image;

    setImageUrl(image);
  };

  const getTags = async () => {
    const response = await axios.get('https://test-api.babble.gg/api/tags');
    const tags = response.data;

    setTagList(tags);
    setAutoCompleteTagList(tags);
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

    const searchResults = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+/g.test(inputValue)
      ? tagList.filter((tag) => {
          const keywordRegExp = getKorRegExp(inputValue, {
            initialSearch: true,
            ignoreSpace: true,
          });
          return tag.name.match(keywordRegExp);
        })
      : tagList.filter((tag) => {
          const searchRegex = new RegExp(inputValue, 'gi');
          const keywordWithoutSpace = tag.name.replace(/\s/g, '');
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
      const response = await axios.post(
        'https://test-api.babble.gg/api/rooms',
        {
          gameId,
          maxHeadCount,
          tags: tagIds,
        }
      );
      const { tags, game, roomId } = response.data;

      closeChatting();
      openChatting(<ChattingRoom tags={tags} game={game} roomId={roomId} />);

      history.push({
        pathname: `${PATH.ROOM_LIST}/${gameId}`,
        state: { gameName },
      });
    } catch (error) {
      alert('방 생성을 하는 데 오류가 있습니다.');
    }
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
              maxHeadCount={maxHeadCount}
              setMaxHeadCount={setMaxHeadCount}
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
            <Link
              to={{
                pathname: `${PATH.ROOM_LIST}/${gameId}`,
                state: { gameName },
              }}
            >
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
