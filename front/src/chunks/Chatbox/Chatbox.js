import './Chatbox.scss';

import React, { useEffect, useRef } from 'react';

import ChattingForm from '../../components/ChattingForm/ChattingForm';
import PropTypes from 'prop-types';

const Chatbox = ({ roomId, createdAt, onSubmit, children }) => {
  const chattingsRef = useRef(null);

  useEffect(() => {
    chattingsRef.current.scrollTop = chattingsRef.current.scrollHeight;
  }, [children]);

  return (
    <section className='chatbox-container'>
      <section className='chattings' ref={chattingsRef}>
        <article className='chatting-room-info'>
          <span>{`${roomId}번 방`}</span>
          <time>{createdAt.toLocaleString('ko-KR')}</time>
        </article>
        <article className='chatting-contents'>{children}</article>
      </section>
      <section className='chatting-form'>
        <ChattingForm onSubmit={onSubmit} />
      </section>
    </section>
  );
};

Chatbox.propTypes = {
  roomId: PropTypes.number,
  createdAt: PropTypes.string,
  onSubmit: PropTypes.func,
  children: PropTypes.node,
};

export default Chatbox;
