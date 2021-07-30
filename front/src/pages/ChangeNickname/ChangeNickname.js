import './ChangeNickname.scss';

import Caption1 from '../../core/Typography/Caption1';
import { IoCloseOutline } from 'react-icons/io5';
import React from 'react';
import RoundButton from '../../components/Button/RoundButton';
import Subtitle3 from '../../core/Typography/Subtitle3';
import TextInput from '../../components/SearchInput/TextInput';
import { useModal } from '../../contexts/ModalProvider';
import { useUser } from '../../contexts/UserProvider';

const ChangeNickname = () => {
  const { close } = useModal();
  const { changeNickname } = useUser();

  const submitNickname = (e) => {
    e.preventDefault();
    const nicknameInput = e.target.nickname.value;

    changeNickname(nicknameInput);
    close();
  };

  return (
    <form className='nickname-container' onSubmit={submitNickname}>
      <div onClick={close} className='control-bar'>
        <IoCloseOutline size='24px' />
      </div>
      <div className='control-inputs'>
        <Subtitle3>닉네임 변경하기</Subtitle3>
        <TextInput name='nickname' placeholder='닉네임을 입력해주세요.' />
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
