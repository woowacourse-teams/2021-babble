import './ChangeNickname.scss';

import { Caption1, Subtitle3 } from '../../core/Typography';
import { NICKNAME_MAX_LENGTH, NICKNAME_MIN_LENGTH } from '../../constants/chat';
import { RoundButton, TextInput } from '../../components';

import { IoCloseOutline } from 'react-icons/io5';
import React from 'react';
import axios from 'axios';
import { setLocalStorage } from '../../utils/localStorage';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';
import { useUser } from '../../contexts/UserProvider';

const ChangeNickname = () => {
  const { closeModal } = useDefaultModal();
  const { user, changeUser, setIsNicknameChanged } = useUser();

  const submitNickname = async (e) => {
    e.preventDefault();
    const nicknameInput = e.target.nickname.value;

    const response = await axios.post('https://babble-test.o-r.kr/api/users', {
      nickname: nicknameInput,
    });
    const generatedUser = response.data;

    setLocalStorage('nickname', generatedUser.nickname);

    setIsNicknameChanged(true);
    changeUser({ id: generatedUser.id, nickname: generatedUser.nickname });
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
          maxLength={NICKNAME_MAX_LENGTH}
          minLength={NICKNAME_MIN_LENGTH}
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
