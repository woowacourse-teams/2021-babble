import './RoomList.scss';

import Body1 from '../../core/Typography/Body1';
import Logo from '../../core/Logo/Logo';
import ModalChattingRoom from '../../components/Modal/ModalChattingRoom';
import React from 'react';
import SquareButton from '../../components/Button/SquareButton';
import data from '../../../mockData.json';
// import axios from 'axios';
import { useModal } from '../../contexts/ModalProvider';

const RoomList = () => {
  const { open } = useModal();

  const openChatting = async () => {
    try {
      // const response = await axios.get('/mockData.json');
      // const data = await response.json();
      const {
        tags,
        host: { id, name, avatar },
        roomId,
        createdDate,
      } = data.roomInfo;
      open(
        <ModalChattingRoom
          tags={tags}
          participants={{ host: { id, name, profileImg: avatar } }}
          roomNo={roomId}
          createdAt={createdDate}
        />
      ); // 데이터 집어넣기
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className='room-list-container'>
      <span className='babble-logo'>
        <Logo width={100} />
      </span>
      <SquareButton colored={false} size='large' onClick={openChatting}>
        <Body1>방 생성</Body1>
      </SquareButton>
      <SquareButton colored={false} size='large'>
        <Body1>방 참가</Body1>
      </SquareButton>
    </div>
  );
};

export default RoomList;
