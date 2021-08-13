import AvatarImage from '../Avatar/AvatarImage';
import { Caption2 } from '../../core/Typography';
import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import React from 'react';
import SpeechBubble from './SpeechBubble';

const SpeechBubbleWithAvatar = ({ size = 'small', time, user, children }) => {
  return (
    <div className='speech-bubble-container'>
      <LinearLayout direction='row'>
        <AvatarImage imageSrc={user.avatar} size={size} />
        <LinearLayout direction='col'>
          <Caption2>{user.nickname}</Caption2>
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
  user: PropTypes.shape({
    id: PropTypes.number,
    nickname: PropTypes.string,
    avatar: PropTypes.string,
  }),
  children: PropTypes.node,
};

export default SpeechBubbleWithAvatar;
