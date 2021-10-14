import './Participants.scss';

import { Avatar, Badge } from '../../components';
import React, { useState } from 'react';

import { Caption2 } from '../../core/Typography';
import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import { RiVipCrown2Fill } from '@react-icons/all-files/ri/RiVipCrown2Fill';
import { VscCircleFilled } from '@react-icons/all-files/vsc/VscCircleFilled';
import { useUser } from '../../contexts/UserProvider';

const Participants = ({ participants }) => {
  const { user } = useUser();
  const [copySuccess, setCopySuccess] = useState(false);
  const [copyFailed, setCopyFailed] = useState(false);

  const copyNickname = (e) => {
    const nickname = e.target.textContent;

    if (navigator.clipboard) {
      navigator.clipboard.writeText(nickname);

      setCopySuccess(true);
      setCopyFailed(false);
    } else {
      setCopySuccess(false);
      setCopyFailed(true);
    }

    e.target.classList.add('animate');

    setTimeout(() => {
      e.target.classList.remove('animate');
      setCopySuccess(false);
      setCopyFailed(false);
    }, 500);
  };

  return (
    <aside className='participants-container'>
      <LinearLayout>
        {participants?.host && (
          <Avatar direction='row' imageSrc={participants?.host?.avatar}>
            <Caption2 onClick={copyNickname}>
              {participants?.host?.nickname}
            </Caption2>
            <Badge colored={user.id === participants?.host?.id}>
              <RiVipCrown2Fill size='12px' />
            </Badge>
          </Avatar>
        )}
        {participants?.guests?.map(({ id, avatar, nickname }, index) => (
          <Avatar direction='row' key={index} imageSrc={avatar}>
            <Caption2 onClick={copyNickname}>{nickname}</Caption2>
            {user.id === id && (
              <Badge colored>
                <VscCircleFilled size='12px' />
              </Badge>
            )}
          </Avatar>
        ))}
        <div
          className={`notice${copySuccess || copyFailed ? ' animate' : ''}${
            copyFailed ? ' failed' : ''
          }`}
        >
          <Caption2>
            {copySuccess && '닉네임이 복사되었습니다!'}
            {copyFailed && '닉네임 복사에 실패했습니다.'}
          </Caption2>
        </div>
      </LinearLayout>
    </aside>
  );
};

Participants.propTypes = {
  participants: PropTypes.shape({
    host: PropTypes.shape({
      id: PropTypes.number,
      nickname: PropTypes.string,
      avatar: PropTypes.string,
    }),
    guests: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.number,
        nickname: PropTypes.string,
        avatar: PropTypes.string,
      })
    ),
  }),
};

export default Participants;
