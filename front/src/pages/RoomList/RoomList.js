import './RoomList.scss';

import Body1 from '../../core/Typography/Body1';
import Body2 from '../../core/Typography/Body2';
import ChattingRoom from '../ChattingRoom/ChattingRoom';
import Headline2 from '../../core/Typography/Headline2';
import MainImage from '../../components/MainImage/MainImage';
import NicknameSection from '../../components/NicknameSection/NicknameSection';
import PageLayout from '../../core/Layout/PageLayout';
import PropTypes from 'prop-types';
import React from 'react';
import Room from '../../components/Room/Room';
import SearchInput from '../../components/SearchInput/SearchInput';
import SquareButton from '../../components/Button/SquareButton';
import TagList from '../../chunks/TagList/TagList';
import axios from 'axios';
import { useModal } from '../../contexts/ModalProvider';

const RoomList = ({
  mainImage = 'https://images.igdb.com/igdb/image/upload/t_1080p/co254s.jpg',
}) => {
  const { open } = useModal();
  const roomList = [
    {
      roomId: 1,
      createdDate: '2021-07-05T00:00:00.000Z',
      game: {
        id: 10,
        name: 'Apex Legend',
      },
      host: {
        id: 1234,
        name: 'wilder',
      },
      headCount: {
        current: 3,
        max: 20,
      },
      tags: [
        {
          name: '2시간',
        },
        {
          name: '실버',
        },
      ],
    },
    {
      roomId: 2,
      createdDate: '2021-07-05T00:00:00.000Z',
      game: {
        id: 11,
        name: 'Apex Legend',
      },
      host: {
        id: 1111,
        name: 'hyun9macasdfasdfasdfasdkasdfasdfck',
      },
      headCount: {
        current: 1,
        max: 10,
      },
      tags: [
        {
          name: '2시간',
        },
        {
          name: '실버',
        },
      ],
    },
  ];

  const createChatting = async () => {
    try {
      const response = await axios.post('https://babble.o-r.kr/api/rooms', {
        gameId: 1,
        tags: [{ name: '실버' }, { name: '2시간' }, { name: '솔로랭크' }],
      });
      const { tags, roomId, createdDate } = response.data;
      open(
        <ChattingRoom
          tags={tags}
          participants={{}}
          roomId={roomId}
          createdAt={createdDate}
        />,
        'chatting'
      );
    } catch (error) {
      alert('방 생성을 하는 데 오류가 있습니다.');
    }
  };

  const joinChatting = async () => {
    const roomIdInput = window.prompt('입장할 방 번호를 입력해주세요.');
    try {
      const response = await axios.get(
        `https://babble.o-r.kr/api/rooms/${roomIdInput}`
      );

      const { tags, roomId, createdDate } = response.data;
      open(
        <ChattingRoom
          tags={tags}
          participants={{}}
          roomId={roomId}
          createdAt={createdDate}
        />,
        'chatting'
      );
    } catch (error) {
      alert('방이 존재하지 않습니다.');
      console.log(error);
    }
  };

  return (
    <div className='room-list-container'>
      <MainImage imageSrc={mainImage} />
      <PageLayout>
        <section className='room-list-header'>
          <Headline2>{'League of Legends'}</Headline2>
          <div className='side'>
            <NicknameSection nickname={'wilder'} />
            <SquareButton size='medium' onClick={createChatting} colored>
              <Body2>방 생성하기</Body2>
            </SquareButton>
          </div>
        </section>

        <section className='search-section'>
          <SearchInput autoCompleteKeywords={[]} />
          <TagList
            tags={[
              { name: '피터' },
              { name: '바보' },
              { name: '안현철' },
              { name: '더바보' },
            ]}
            erasable
          />
        </section>

        <section className='room-list-section'>
          {roomList.map((room, index) => (
            <Room room={room} key={index} />
          ))}
        </section>

        <SquareButton
          style={{ display: 'none' }}
          colored={false}
          size='large'
          onClick={joinChatting}
        >
          <Body1>방 참가</Body1>
        </SquareButton>
      </PageLayout>
    </div>
  );
};

RoomList.propTypes = {
  mainImage: PropTypes.string,
};

export default RoomList;
