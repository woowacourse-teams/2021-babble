import './NicknameSection.scss';

import React, { useEffect, useState } from 'react';

import Body2 from '../../core/Typography/Body2';
import Caption1 from '../../core/Typography/Caption1';
import { FiEdit } from 'react-icons/fi';
import PropTypes from 'prop-types';

const NicknameSection = ({ nickname }) => {
  const [customNickname, setCustomNickname] = useState('');

  useEffect(() => {
    if (!customNickname) return;
    setCustomNickname(nickname);
  }, []);

  // customNickname이 있으면(유저가 직접 닉네임을 정하면) 말풍선 없애기. 아니면 띄우기

  return (
    <section className='nickname-section-container'>
      <div className='notice-bubble'>
        <Caption1>닉네임을 변경해주세요!</Caption1>
      </div>
      <Body2>{nickname}</Body2>
      <button className='edit'>
        <FiEdit size='18px' />
      </button>
      <Caption1>님 안녕하세요!</Caption1>
    </section>
  );
};

NicknameSection.propTypes = {
  nickname: PropTypes.string,
};

export default NicknameSection;
