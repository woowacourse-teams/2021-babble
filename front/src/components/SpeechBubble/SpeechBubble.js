import './SpeechBubble.scss';

import PropTypes from 'prop-types';
import React from 'react';

const SpeechBubble = ({ time, type = 'others', children }) => {
  return (
    <div className={`speech-bubble ${type}`}>
      <div className='text'>{children}</div>
      <time className='time'>{time}</time>
    </div>
  );
};

SpeechBubble.propTypes = {
  time: PropTypes.instanceOf(Date),
  type: PropTypes.string,
  children: PropTypes.node,
};

export default SpeechBubble;
