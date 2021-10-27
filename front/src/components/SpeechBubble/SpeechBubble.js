import './SpeechBubble.scss';

import { PATTERNS } from '../../constants/regex';
import PropTypes from 'prop-types';
import React from 'react';
import ReactHtmlParser from 'react-html-parser';

const SpeechBubble = ({ time, type = 'mine', children }) => {
  const linkReg = new RegExp(PATTERNS.LINKS);
  const linkArray = children.match(linkReg);
  const chat = { children: children };

  if (linkArray) {
    chat.children = '';
    const innerElement = linkArray.map((link) =>
      children.replace(
        link,
        `<a href="${link}" target='_blank' rel='noopener noreferrer'>${link}</a>`
      )
    );

    chat.children = innerElement.join('');

    if (innerElement) {
      chat.children = ReactHtmlParser(chat.children);
    }
  }

  return (
    <div
      className={`speech-bubble-container ${type}-container ${
        !time && 'no-time'
      }`}
    >
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
