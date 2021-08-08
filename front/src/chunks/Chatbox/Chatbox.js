import './Chatbox.scss';

import React, { useEffect, useRef } from 'react';

import { ChattingForm } from '../../components';
import PropTypes from 'prop-types';

const Chatbox = ({ onSubmit, children }) => {
  const chattingsRef = useRef(null);

  useEffect(() => {
    chattingsRef.current.scrollTop = chattingsRef.current.scrollHeight;
  }, [children]);

  return (
    <section className='chatbox-container'>
      <section className='chattings' ref={chattingsRef}>
        <article className='chatting-contents'>{children}</article>
      </section>
      <section className='chatting-form'>
        <ChattingForm onSubmit={onSubmit} />
      </section>
    </section>
  );
};

Chatbox.propTypes = {
  onSubmit: PropTypes.func,
  children: PropTypes.node,
};

export default Chatbox;
