import './Room.scss';

import Avatar from '../Avatar/Avatar';
import Body2 from '../../core/Typography/Body2';
import PropTypes from 'prop-types';
import React from 'react';
import TagList from '../../chunks/TagList/TagList';

const Room = ({
  roomId,
  imageSrc,
  nickname,
  headCount,
  totalHeadCount,
  tags,
}) => {
  return (
    <section className='room-container'>
      <section className='information'>
        <div className='about'>
          <div className='room-number'>
            <Body2>{roomId}</Body2>
          </div>
          <div className='avatar'>
            <Avatar size='small' imageSrc={imageSrc} direction='row'>
              <Body2>{nickname}</Body2>
            </Avatar>
          </div>
          <TagList tags={tags} />
        </div>
        <div className='headcount-container'>
          <Body2>
            <strong>{headCount}</strong> / {totalHeadCount}
          </Body2>
        </div>
      </section>
    </section>
  );
};

Room.propTypes = {
  roomId: PropTypes.number,
  imageSrc: PropTypes.string,
  nickname: PropTypes.string,
  headCount: PropTypes.number,
  totalHeadCount: PropTypes.number,
  tags: PropTypes.arrayOf(PropTypes.string),
};

export default Room;
