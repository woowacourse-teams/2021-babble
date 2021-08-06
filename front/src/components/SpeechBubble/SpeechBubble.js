import './SpeechBubble.scss';

import PropTypes from 'prop-types';
import React from 'react';

const SpeechBubble = ({ time, type = 'mine', children }) => {
  return (
    <div className={`speech-bubble-container ${type}-container`}>
      <div className={`speech-bubble ${type}`}>
        <pre className='text'>{children}</pre>
        <time className='time'>{time}</time>
      </div>
    </div>
  );
};

SpeechBubble.propTypes = {
  time: PropTypes.string,
  type: PropTypes.string,
  children: PropTypes.node,
};

export default SpeechBubble;
