import './ChattingForm.scss';

import Caption1 from '../../core/Typography/Caption1';
import React from 'react';
import SquareButton from '../Button/SquareButton';

const ChattingForm = () => {
  return (
    <form className='chatting-form'>
      <label htmlFor='chat-textarea'></label>
      <textarea
        id='chat-textarea'
        className='chat-area'
        rows='2'
        aria-label='chatting-text-area'
      ></textarea>
      <SquareButton size='block' type='submit'>
        <Caption1>전송하기</Caption1>
      </SquareButton>
    </form>
  );
};

export default ChattingForm;
