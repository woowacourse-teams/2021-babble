import './Room.scss';

import Avatar from '../Avatar/Avatar';
import { Body2 } from '../../core/Typography';
import PropTypes from 'prop-types';
import React from 'react';
import TagList from '../../chunks/TagList/TagList';
import { useUser } from '../../contexts/UserProvider';

const Room = ({ room, onClickRoom, scrollRef }) => {
  const { roomId, host, headCount, tags } = room;
  const { current, max } = headCount;
  const {
    user: { currentRoomNumber },
  } = useUser();

  const isDisabled = currentRoomNumber === roomId || max === current;

  return (
    <div className='room-wrapper' ref={scrollRef}>
      <div className={`room-shade ${isDisabled ? 'disabled' : ''}`}></div>
      <section
        className={`room-container ${isDisabled ? 'disabled' : ''}`}
        data-room-id={roomId}
        onClick={isDisabled ? null : onClickRoom}
      >
        <section className='information'>
          <div className='about'>
            <div className='room-number'>
              <Body2>{roomId}</Body2>
            </div>
            <div className='avatar'>
              <Avatar size='small' imageSrc={host.avatar} direction='row'>
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
    </div>
  );
};

Room.propTypes = {
  imageSrc: PropTypes.string,
  room: PropTypes.shape({
    roomId: PropTypes.number,
    host: PropTypes.shape({
      id: PropTypes.number,
      nickname: PropTypes.string,
      avatar: PropTypes.string,
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
  scrollRef: PropTypes.object,
};

export default Room;
