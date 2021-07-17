import './RoomList.scss';

import Body1 from '../../core/Typography/Body1';
import Logo from '../../core/Logo/Logo';
import ModalChattingRoom from '../../components/Modal/ModalChattingRoom';
import React from 'react';
import SquareButton from '../../components/Button/SquareButton';
import axios from 'axios';
import { useModal } from '../../contexts/ModalProvider';

const RoomList = () => {
  const { open } = useModal();

  const createChatting = async () => {
    try {
      const response = await axios.post(
        'https://babble-test.o-r.kr/api/rooms',
        {
          gameId: 1,
          tags: ['실버', '2시간', '솔로랭크'],
        }
      );
      const { tags, roomId, createdDate } = response.data;
      open(
        <ModalChattingRoom
          tags={tags}
          participants={{}}
          roomId={roomId}
          createdAt={createdDate}
        />
      );
    } catch (error) {
      alert('방 생성에 실패했습니다!');
    }
  };

  const joinChatting = async () => {
    const roomIdInput = window.prompt('입장할 방 번호를 입력해주세요.');
    try {
      const response = await axios.get(
        `https://babble-test.o-r.kr/api/rooms/${roomIdInput}`
      );

      const { tags, roomId, createdDate } = response.data;
      open(
        <ModalChattingRoom
          tags={tags}
          participants={{}}
          roomId={roomId}
          createdAt={createdDate}
        />
      );
    } catch (error) {
      alert('방이 존재하지 않습니다.');
      console.log(error);
    }
  };

  return (
    <div className='room-list-container'>
      <span className='babble-logo'>
        <Logo width={100} />
      </span>
      <SquareButton colored={false} size='large' onClick={createChatting}>
        <Body1>방 생성</Body1>
      </SquareButton>
      <SquareButton colored={false} size='large' onClick={joinChatting}>
        <Body1>방 참가</Body1>
      </SquareButton>
    </div>
  );
};

export default RoomList;
