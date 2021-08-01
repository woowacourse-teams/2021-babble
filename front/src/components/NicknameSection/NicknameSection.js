import './NicknameSection.scss';

import Body2 from '../../core/Typography/Body2';
import Caption1 from '../../core/Typography/Caption1';
import Caption2 from '../../core/Typography/Caption2';
import ChangeNickname from '../../pages/ChangeNickname/ChangeNickname';
import { FiEdit } from 'react-icons/fi';
import React from 'react';
import { useChattingModal } from '../../contexts/ChattingModalProvider';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';
import { useUser } from '../../contexts/UserProvider';

const NicknameSection = () => {
  const { openModal } = useDefaultModal();
  const { isChattingModalOpen } = useChattingModal();
  const {
    user: { nickname },
    isNicknameChanged,
  } = useUser();

  const openNicknameModal = () => {
    openModal(<ChangeNickname />);
  };

  return (
    <section className='nickname-section-container'>
      {!isNicknameChanged && (
        <div className='notice-bubble'>
          <Caption2>닉네임을 변경해주세요!</Caption2>
        </div>
      )}
      <Body2>{nickname}</Body2>
      <button
        className='edit'
        onClick={openNicknameModal}
        disabled={isChattingModalOpen}
      >
        <FiEdit size='16px' />
      </button>
      <Caption1>님 안녕하세요!</Caption1>
    </section>
  );
};

export default NicknameSection;
