import './Chatbox.scss';

import ChattingForm from '../../components/ChattingForm/ChattingForm';
import PropTypes from 'prop-types';
import React from 'react';

const Chatbox = ({ roomNo, createdAt, children }) => {
  return (
    <main className='chatbox-container'>
      <section className='chattings'>
        <article className='chatting-room-info'>
          <span>{`${roomNo}번 방`}</span>
          <time>{createdAt.toLocaleString('ko-KR')}</time>
        </article>
        <article className='chatting-contents'>{children}</article>
      </section>
      <section className='chatting-form'>
        <ChattingForm />
      </section>
    </main>
  );
};

Chatbox.propTypes = {
  roomNo: PropTypes.number,
  createdAt: PropTypes.string,
  children: PropTypes.node,
};

export default Chatbox;