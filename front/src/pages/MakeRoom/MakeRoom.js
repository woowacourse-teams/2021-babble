import './MakeRoom.scss';

import { Link, useHistory } from 'react-router-dom';
import React, { useEffect, useState } from 'react';

import Body2 from '../../core/Typography/Body2';
import ChattingRoom from '../ChattingRoom/ChattingRoom';
import DropdownInput from '../../components/SearchInput/DropdownInput';
import Headline2 from '../../core/Typography/Headline2';
import MainImage from '../../components/MainImage/MainImage';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import PropTypes from 'prop-types';
import RoundButton from '../../components/Button/RoundButton';
import SearchInput from '../../components/SearchInput/SearchInput';
import TagList from '../../chunks/TagList/TagList';
import axios from 'axios';
import { useChattingModal } from '../../contexts/ChattingModalProvider';

const MakeRoom = ({ gameId }) => {
  const [imageUrl, setImageUrl] = useState('');
  const [tagList, setTagList] = useState([]);
  const [selectedTagList, setSelectedTagList] = useState([]);
  const [maxHeadCount, setMaxHeadCount] = useState(0);
  const { open } = useChattingModal();
  const history = useHistory();

  const getImage = async () => {
    const response = await axios.get(
      `https://babble-test.o-r.kr/api/games/${gameId}/images`
    );
    const image = response.data.image;

    setImageUrl(image);
  };

  const getTags = async () => {
    const response = await axios.get('https://babble-test.o-r.kr/api/tags');
    const tags = response.data;

    setTagList(tags);
  };

  const selectTag = (tagName) => {
    const tagId = tagList.find((tag) => tag.name === tagName).id;
    const tag = { id: tagId, name: tagName };

    setSelectedTagList((prevTagList) => [...prevTagList, tag]);
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

  const createRoom = async (e) => {
    e.preventDefault();

    try {
      // TODO: 테스트 서버에서 실제 배포 서버로 변경하기
      const tagIds = selectedTagList.map(({ id }) => ({ id }));
      const response = await axios.post(
        'https://babble-test.o-r.kr/api/rooms',
        {
          gameId,
          maxHeadCount,
          tags: tagIds,
        }
      );
      const { tags, roomId, createdDate } = response.data;

      open(
        <ChattingRoom tags={tags} roomId={roomId} createdAt={createdDate} />
      );

      history.push({ pathname: PATH.HOME });
    } catch (error) {
      alert('방 생성을 하는 데 오류가 있습니다.');
    }
  };

  useEffect(() => {
    getImage();
    getTags();
  }, []);

  return (
    <form className='make-room-container' onSubmit={createRoom}>
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
            autoCompleteKeywords={tagList}
            onClickKeyword={selectTag}
          />
        </section>
        <section className='tag-list-section'>
          <TagList tags={selectedTagList} onDeleteTag={eraseTag} erasable />
        </section>
        <section className='buttons'>
          <Link to={PATH.HOME}>
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
  );
};

MakeRoom.propTypes = {
  gameId: PropTypes.number,
};

export default MakeRoom;
