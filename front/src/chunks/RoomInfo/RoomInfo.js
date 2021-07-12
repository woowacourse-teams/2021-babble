import './RoomInfo.scss';

import AngledButton from '../../components/Button/AngledButton';
import Avatar from '../../components/Avatar/Avatar';
import Logo from '../../components/Logo/Logo';
import PropTypes from 'prop-types';
import React from 'react';
import Tag from '../../components/Tag/Tag';

const RoomInfo = ({ gameTitle, host, guests, tags }) => {
  return (
    <aside className='room-info'>
      <section className='room-info-container'>
        <article className='logo-container'>
          <Logo />
        </article>
        <article className='room-info-content'>
          <h1 className='game-title'>{gameTitle}</h1>
          <div className='host'>
            <Avatar
              size='large'
              nickName={host.nickName}
              imageSrc={host.avatar}
            />
          </div>
          <div className='guests'>
            {guests.map((guest, index) => (
              <Avatar
                key={index}
                size='small'
                nickName={guest.nickName}
                imageSrc={guest.avatar}
              />
            ))}
          </div>
          <div className='tags'>
            {tags.map((tag, index) => (
              <Tag key={index}>{tag}</Tag>
            ))}
          </div>
        </article>
      </section>
      <section className='exit-button-container'>
        <AngledButton>방 나가기</AngledButton>
      </section>
    </aside>
  );
};

RoomInfo.propTypes = {
  gameTitle: PropTypes.string,
  host: PropTypes.shape({
    nickName: PropTypes.string,
    avatar: PropTypes.string,
  }),
  guests: PropTypes.arrayOf(
    PropTypes.shape({
      nickName: PropTypes.string,
      avatar: PropTypes.string,
    })
  ),
  tags: PropTypes.arrayOf(PropTypes.string),
};

export default RoomInfo;
