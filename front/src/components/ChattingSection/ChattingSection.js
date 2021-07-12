import './ChattingSection.scss';

import AngledButton from '../Button/AngledButton';
import React from 'react';

const ChattingSection = () => {
  return (
    <form className='chatting-form'>
      <textarea className='chat-area' rows='3'></textarea>
      <AngledButton type='submit'>Send</AngledButton>
    </form>
  );
};

export default ChattingSection;
