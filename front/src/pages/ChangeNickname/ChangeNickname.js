import './ChangeNickname.scss';

import { Caption1, Subtitle3 } from '../../core/Typography';
import { RoundButton, TextInput } from '../../components';

import { IoCloseOutline } from 'react-icons/io5';
import React from 'react';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';
import { useUser } from '../../contexts/UserProvider';

const ChangeNickname = () => {
  const { closeModal } = useDefaultModal();
  const { user, changeUserNickname, setIsNicknameChanged } = useUser();

  const submitNickname = (e) => {
    e.preventDefault();
    const nicknameInput = e.target.nickname.value;

    setIsNicknameChanged(true);
    changeUserNickname(nicknameInput);
    closeModal();
  };

  return (
    <form className='nickname-container' onSubmit={submitNickname}>
      <div onClick={closeModal} className='control-bar'>
        <IoCloseOutline size='24px' />
      </div>
      <div className='control-inputs'>
        <Subtitle3>닉네임 변경하기</Subtitle3>
        <TextInput
          value={user.nickname}
          name='nickname'
          placeholder='닉네임을 입력해주세요.'
          isContentSelected
        />
      </div>
      <div className='control-buttons'>
        <RoundButton onClick={closeModal} size='small'>
          <Caption1>취소하기</Caption1>
        </RoundButton>
        <RoundButton type='submit' size='small' colored>
          <Caption1>변경하기</Caption1>
        </RoundButton>
      </div>
    </form>
  );
};

export default ChangeNickname;
