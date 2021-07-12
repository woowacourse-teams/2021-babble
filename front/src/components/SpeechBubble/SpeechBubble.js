import './SpeechBubble.scss';

import PropTypes from 'prop-types';
import React from 'react';

const SpeechBubble = ({ time, type = 'others', children }) => {
  return (
    <div className={`speech-bubble ${type}`}>
      <pre className='text'>{children}</pre>
      <time className='time'>{time}</time>
    </div>
  );
};

SpeechBubble.propTypes = {
  time: PropTypes.string,
  type: PropTypes.string,
  children: PropTypes.node,
};

export default SpeechBubble;
