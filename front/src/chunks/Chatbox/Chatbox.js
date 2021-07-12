import './Chatbox.scss';

import ChattingSection from '../../components/ChattingSection/ChattingSection';
import PropTypes from 'prop-types';
import React from 'react';

const Chatbox = ({ roomNo, createdAt, children }) => {
  return (
    <main className='chatbox-container'>
      <section className='chatbox-chattings'>
        <article className='chatbox-room-info'>
          <span>{`${roomNo}번 방`}</span>
          <time>{createdAt.toLocaleString('ko-KR')}</time>
        </article>
        <article className='chatbox-chatting-contents'>{children}</article>
      </section>
      <section className='chatbox-chatting-form'>
        <ChattingSection />
      </section>
    </main>
  );
};

Chatbox.propTypes = {
  roomNo: PropTypes.number,
  createdAt: PropTypes.instanceOf(Date),
};

export default Chatbox;
