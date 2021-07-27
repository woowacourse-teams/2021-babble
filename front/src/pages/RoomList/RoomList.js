import './RoomList.scss';

import Body1 from '../../core/Typography/Body1';
import ChattingRoom from '../ChattingRoom/ChattingRoom';
import Logo from '../../core/Logo/Logo';
import ModalNickname from '../../components/Modal/ModalNickname';
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
        `https://babble-test.o-r.kr/api/rooms/${roomIdInput}`
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

  const openNicknameModal = () => {
    open(<ModalNickname />);
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
      <SquareButton colored={false} size='large' onClick={openNicknameModal}>
        <Body1>닉네임 생성</Body1>
      </SquareButton>
    </div>
  );
};

export default RoomList;
