import './NicknameSection.scss';

import Body2 from '../../core/Typography/Body2';
import Caption1 from '../../core/Typography/Caption1';
import Caption2 from '../../core/Typography/Caption2';
import ChangeNickname from '../../pages/ChangeNickname/ChangeNickname';
import { FiEdit } from 'react-icons/fi';
import PropTypes from 'prop-types';
import React from 'react';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const NicknameSection = ({ nickname }) => {
  const { open } = useDefaultModal();

  const openNicknameModal = () => {
    open(<ChangeNickname />);
  };

  return (
    <section className='nickname-section-container'>
      {nickname && (
        <div className='notice-bubble'>
          <Caption2>닉네임을 변경해주세요!</Caption2>
        </div>
      )}
      <Body2>{nickname}</Body2>
      <button className='edit' onClick={openNicknameModal}>
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
