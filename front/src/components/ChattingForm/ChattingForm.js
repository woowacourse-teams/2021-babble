import './ChattingForm.scss';

import { Caption1, Caption2 } from '../../core/Typography';
import React, { useState } from 'react';

import PropTypes from 'prop-types';
import SquareButton from '../Button/SquareButton';

const ChattingForm = ({ onSubmit }) => {
  const [overMaxLength, setOverMaxLength] = useState(false);
  const [textLength, setTextLength] = useState(0);

  const onEnterSubmit = (e) => {
    if (e.isComposing || e.keyCode === 229) return;
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();

      const sendButton = e.currentTarget.send;
      sendButton.click();
      return false;
    }
  };

  const blockWhenMaxLength = (e) => {
    if (e.currentTarget.value.length > 300) {
      e.currentTarget.classList.add('alert');
      setTextLength(e.currentTarget.value.length);
      setOverMaxLength(true);
      return;
    }

    e.currentTarget.classList.remove('alert');
    setOverMaxLength(false);
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
        onChange={blockWhenMaxLength}
        autoFocus
      ></textarea>
      {overMaxLength && (
        <span className='alert-container'>
          <img src='https://babble.gg/img/icons/warning.png' alt='warning' />
          <span className='alert-text'>
            <Caption2>{textLength} / 300 자</Caption2>
          </span>
        </span>
      )}

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
