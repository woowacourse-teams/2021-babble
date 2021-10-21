import './SpeechBubble.scss';

import PropTypes from 'prop-types';
import React from 'react';

const SpeechBubble = ({ time, type = 'mine', children }) => {
  const chat = { children: null };

  if (children.includes('https://') || children.includes('http://')) {
    chat.children = (
      <a href={children} target='_blank' rel='noopener noreferrer'>
        {children}
      </a>
    );
    console.log(chat);
  } else {
    chat.children = children;
  }
  return (
    <div className={`speech-bubble-container ${type}-container`}>
      <div className={`speech-bubble ${type}`}>
        <pre className='text'>{chat.children}</pre>
        <time className='time'>{time}</time>
      </div>
    </div>
  );
};

SpeechBubble.propTypes = {
  time: PropTypes.string,
  type: PropTypes.oneOf(['mine', 'others', 'notice']),
  children: PropTypes.node,
};

export default SpeechBubble;
