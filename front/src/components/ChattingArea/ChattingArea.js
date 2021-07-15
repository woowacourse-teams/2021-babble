import './ChattingArea.scss';

import Body2 from '../../core/Typography/Body2';
import React from 'react';
import SquareButton from '../Button/SquareButton';

const ChattingArea = () => {
  return (
    <form className='chatting-form'>
      <textarea className='chat-area' rows='3'></textarea>
      <SquareButton size='block' type='submit'>
        <Body2>전송하기</Body2>
      </SquareButton>
    </form>
  );
};

export default ChattingArea;
