import './RoomList.scss';

import React, { useEffect, useState } from 'react';

import Body2 from '../../core/Typography/Body2';
import ChattingRoom from '../ChattingRoom/ChattingRoom';
import Headline2 from '../../core/Typography/Headline2';
import { Link } from 'react-router-dom';
import { MODAL_TYPE_CHATTING } from '../../constants/chat';
import MainImage from '../../components/MainImage/MainImage';
import NicknameSection from '../../components/NicknameSection/NicknameSection';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import PropTypes from 'prop-types';
import Room from '../../components/Room/Room';
import SearchInput from '../../components/SearchInput/SearchInput';
import SquareButton from '../../components/Button/SquareButton';
import TagList from '../../chunks/TagList/TagList';
import axios from 'axios';
import { useModal } from '../../contexts/ModalProvider';

const RoomList = ({ gameId }) => {
  const [imageUrl, setImageUrl] = useState('');
  const [tagList, setTagList] = useState([]);
  const [selectedTagList, setSelectedTagList] = useState([]);
  const [roomList, setRoomList] = useState([]);
  const { open } = useModal();

  const getImage = async () => {
    const { image } = await axios.get(
      `https://babble-test.o-r.kr/api/games/${gameId}/images`
    );

    setImageUrl(image);
  };

  const getTags = async () => {
    const tags = await axios.get('https://babble-test.o-r.kr/api/tags');

    setTagList(tags);
  };

  const getRooms = async (tagIds) => {
    const response = await axios.get('https://babble-test.o-r.kr/api/rooms', {
      params: { gameId: 1, tagIds, page: 1 },
    });
    const rooms = await response.json();

    setRoomList(rooms);
  };

  const selectTag = (tagName) => {
    const tagId = tagList.find((tag) => tag.name === tagName).id;
    const tag = { id: tagId, name: tagName };

    setSelectedTagList((prevTagList) => [...prevTagList, tag]);
  };

  const eraseTag = (e) => {
    // TODO: selectedTag 찾는 로직 고민해보기(element 구조에 종속적임)
    const selectedTag = e.target
      .closest('.tag-container')
      .querySelector('.caption1').textContent;

    setSelectedTagList((prevTagList) => [
      ...prevTagList.filter((tag) => tag !== selectedTag),
    ]);
  };

  const joinChatting = async (e) => {
    const selectedRoomId = e.target.closest('.room-container').dataset.roomId;

    try {
      const response = await axios.get(
        `https://babble-test.o-r.kr/api/rooms/${selectedRoomId}`
      );

      const { tags, roomId, createdDate } = response.data;
      open(
        <ChattingRoom
          tags={tags}
          participants={{}}
          roomId={roomId}
          createdAt={createdDate}
        />,
        MODAL_TYPE_CHATTING
      );
    } catch (error) {
      alert('방이 존재하지 않습니다.');
      console.log(error);
    }
  };

  useEffect(() => {
    getImage();
    getRooms('');
    getTags();
  }, []);

  useEffect(() => {
    const selectedTagIdParam = selectedTagList.map(({ id }) => id).join(',');
    getRooms(selectedTagIdParam);
  }, [selectedTagList]);

  return (
    <div className='room-list-container'>
      <MainImage imageSrc={imageUrl} />
      <PageLayout>
        <section className='room-list-header'>
          <Headline2>{'League of Legends'}</Headline2>
          <div className='side'>
            <NicknameSection nickname={'wilder'} />
            <Link to={PATH.MAKE_ROOM}>
              <SquareButton size='medium' colored>
                <Body2>방 생성하기</Body2>
              </SquareButton>
            </Link>
          </div>
        </section>

        <section className='search-section'>
          <SearchInput
            autoCompleteKeywords={tagList}
            onClickKeyword={selectTag}
          />
          <TagList tags={selectedTagList} onDeleteTag={eraseTag} erasable />
        </section>

        <section className='room-list-section'>
          {roomList.map((room, index) => (
            <Room room={room} key={index} onClickRoom={joinChatting} />
          ))}
        </section>
      </PageLayout>
    </div>
  );
};

RoomList.propTypes = {
  gameId: PropTypes.number,
};

export default RoomList;
