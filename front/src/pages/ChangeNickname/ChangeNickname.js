import './ChangeNickname.scss';

import Caption1 from '../../core/Typography/Caption1';
import { IoCloseOutline } from 'react-icons/io5';
import React from 'react';
import RoundButton from '../../components/Button/RoundButton';
import Subtitle3 from '../../core/Typography/Subtitle3';
import TextInput from '../../components/SearchInput/TextInput';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';
import { useUser } from '../../contexts/UserProvider';

const ChangeNickname = () => {
  const { close } = useDefaultModal();
  const { user, changeUserNickname } = useUser();

  const submitNickname = (e) => {
    e.preventDefault();
    const nicknameInput = e.target.nickname.value;

    changeUserNickname(nicknameInput);
    close();
  };

  return (
    <form className='nickname-container' onSubmit={submitNickname}>
      <div onClick={close} className='control-bar'>
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
        <RoundButton onClick={close} size='small'>
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
