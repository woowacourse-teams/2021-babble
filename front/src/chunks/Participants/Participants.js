import './Participants.scss';

import Avatar from '../../components/Avatar/Avatar';
import Caption2 from '../../core/Typography/Caption2';
import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import React from 'react';
import Subtitle3 from '../../core/Typography/Subtitle3';

const Participants = ({ participants }) => {
  return (
    <aside className='participants-container'>
      <Subtitle3>참가자</Subtitle3>
      <LinearLayout>
        <Avatar direction='row' imageSrc={participants.host.profileImg}>
          <Caption2>{participants.host.name}</Caption2>
        </Avatar>

        {participants.guests.map(({ profileImg, name }, index) => (
          <Avatar direction='row' key={index} imageSrc={profileImg}>
            <Caption2>{name}</Caption2>
          </Avatar>
        ))}
      </LinearLayout>
    </aside>
  );
};

Participants.propTypes = {
  participants: PropTypes.shape({
    host: PropTypes.shape({
      id: PropTypes.number,
      name: PropTypes.string,
      profileImg: PropTypes.string,
    }),
    guests: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.number,
        name: PropTypes.string,
        profileImg: PropTypes.string,
      })
    ),
  }),
};

export default Participants;
