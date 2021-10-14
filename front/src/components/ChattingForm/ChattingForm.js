import './ChattingForm.scss';

import { Caption1 } from '../../core/Typography';
import PropTypes from 'prop-types';
import React from 'react';
import SquareButton from '../Button/SquareButton';

const ChattingForm = ({ onSubmit }) => {
  const onEnterSubmit = (e) => {
    if (e.isComposing || e.keyCode === 229) return;
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();

      const sendButton = e.currentTarget.send;
      sendButton.click();
      return false;
    }
  };

  return (
    <form
      className='chatting-form'
      onSubmit={onSubmit}
      onKeyDown={onEnterSubmit}
    >
      <label htmlFor='chat-textarea'></label>
      <textarea
        id='chat-textarea'
        className='chat-area'
        name='chat'
        rows='2'
        aria-label='chatting-text-area'
        autoFocus
      ></textarea>
      <SquareButton size='block' type='submit' name='send'>
        <Caption1>전송하기</Caption1>
      </SquareButton>
    </form>
  );
};

ChattingForm.propTypes = {
  onSubmit: PropTypes.func,
};

export default ChattingForm;
