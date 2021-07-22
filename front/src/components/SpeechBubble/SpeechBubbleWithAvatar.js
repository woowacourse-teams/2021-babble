import AvatarImage from '../Avatar/AvatarImage';
import Caption2 from '../../core/Typography/Caption2';
import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import React from 'react';
import SpeechBubble from './SpeechBubble';

const SpeechBubbleWithAvatar = ({
  size = 'small',
  time,
  nickname,
  children,
}) => {
  return (
    <div className='speech-bubble-container'>
      <LinearLayout direction='row'>
        <AvatarImage size={size} />
        <LinearLayout direction='col'>
          <Caption2>{nickname}</Caption2>
          <SpeechBubble type='others' time={time}>
            {children}
          </SpeechBubble>
        </LinearLayout>
      </LinearLayout>
    </div>
  );
};

SpeechBubbleWithAvatar.propTypes = {
  size: PropTypes.string,
  time: PropTypes.string,
  nickname: PropTypes.string,
  children: PropTypes.node,
};

export default SpeechBubbleWithAvatar;
