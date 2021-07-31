import './Participants.scss';

import Avatar from '../../components/Avatar/Avatar';
import Badge from '../../components/Badge/Badge';
import Caption2 from '../../core/Typography/Caption2';
import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import React from 'react';
import { RiVipCrown2Fill } from 'react-icons/ri';
import Subtitle3 from '../../core/Typography/Subtitle3';
import { VscCircleFilled } from 'react-icons/vsc';
import { useUser } from '../../contexts/UserProvider';

const Participants = ({ participants }) => {
  const { user } = useUser();

  return (
    <aside className='participants-container'>
      <Subtitle3>참가자</Subtitle3>
      <LinearLayout>
        {participants?.host && (
          <Avatar direction='row' imageSrc={participants?.host?.profileImg}>
            <Caption2>{participants?.host?.nickname}</Caption2>
            <Badge colored={user.id === participants?.host?.id}>
              <RiVipCrown2Fill size='12px' />
            </Badge>
          </Avatar>
        )}
        {participants?.guests?.map(({ id, profileImg, nickname }, index) => (
          <Avatar direction='row' key={index} imageSrc={profileImg}>
            <Caption2>{nickname}</Caption2>
            {user.id === id && (
              <Badge colored>
                <VscCircleFilled size='12px' />
              </Badge>
            )}
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
      nickname: PropTypes.string,
      profileImg: PropTypes.string,
    }),
    guests: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.number,
        nickname: PropTypes.string,
        profileImg: PropTypes.string,
      })
    ),
  }),
};

export default Participants;
