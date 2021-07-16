import './RoomList.scss';

import Body1 from '../../core/Typography/Body1';
import Logo from '../../core/Logo/Logo';
import React from 'react';
import SquareButton from '../../components/Button/SquareButton';

const RoomList = () => {
  return (
    <div className='room-list-container'>
      <span className='babble-logo'>
        <Logo width='100' />
      </span>
      <SquareButton colored={false} size='large'>
        <Body1>방 생성</Body1>
      </SquareButton>
      <SquareButton colored={false} size='large'>
        <Body1>방 참가</Body1>
      </SquareButton>
    </div>
  );
};

export default RoomList;
