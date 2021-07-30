import './Room.scss';

import Avatar from '../Avatar/Avatar';
import Body2 from '../../core/Typography/Body2';
import PropTypes from 'prop-types';
import React from 'react';
import TagList from '../../chunks/TagList/TagList';

const Room = ({ imageSrc, room, onClickRoom }) => {
  const { roomId, host, headCount, tags } = room;
  const { current, max } = headCount;

  return (
    <section
      className='room-container'
      data-room-id={roomId}
      onClick={onClickRoom}
    >
      <section className='information'>
        <div className='about'>
          <div className='room-number'>
            <Body2>{roomId}</Body2>
          </div>
          <div className='avatar'>
            <Avatar size='small' imageSrc={imageSrc} direction='row'>
              <Body2>{host.nickname}</Body2>
            </Avatar>
          </div>
          <TagList tags={tags} />
        </div>
        <div className='headcount-container'>
          <Body2>
            <strong>{current}</strong> / {max}
          </Body2>
        </div>
      </section>
    </section>
  );
};

Room.propTypes = {
  imageSrc: PropTypes.string,
  room: PropTypes.shape({
    roomId: PropTypes.number,
    host: PropTypes.shape({
      id: PropTypes.number,
      nickname: PropTypes.string,
    }),
    headCount: PropTypes.shape({
      current: PropTypes.number,
      max: PropTypes.number,
    }),
    tags: PropTypes.arrayOf(
      PropTypes.shape({
        name: PropTypes.string,
      })
    ),
  }),
  onClickRoom: PropTypes.func,
};

export default Room;
