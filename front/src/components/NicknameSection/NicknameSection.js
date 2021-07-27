import './NicknameSection.scss';

import Body2 from '../../core/Typography/Body2';
import Caption1 from '../../core/Typography/Caption1';
import { FiEdit } from 'react-icons/fi';
import PropTypes from 'prop-types';
import React from 'react';

const NicknameSection = ({ nickname }) => {
  return (
    <section className='nickname-section-container'>
      {nickname && (
        <div className='notice-bubble'>
          <Caption1>닉네임을 변경해주세요!</Caption1>
        </div>
      )}
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
