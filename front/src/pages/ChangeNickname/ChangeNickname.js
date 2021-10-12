import './ChangeNickname.scss';

import { Caption1, Subtitle3 } from '../../core/Typography';
import { NICKNAME_MAX_LENGTH, NICKNAME_MIN_LENGTH } from '../../constants/chat';
import React, { useState } from 'react';
import { RoundButton, TextInput } from '../../components';

import { BASE_URL } from '../../constants/api';
import { IoCloseOutline } from '@react-icons/all-files/io5/IoCloseOutline';
import { PATTERNS } from '../../constants/regex';
import axios from 'axios';
import { setSessionStorage } from '../../utils/storage';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';
import { useUser } from '../../contexts/UserProvider';

const ChangeNickname = () => {
  const [errorMessage, setErrorMessage] = useState('');
  const [isValidNickname, setIsValidNickname] = useState(false);

  const { closeModal } = useDefaultModal();
  const { user, changeUser, setIsNicknameChanged } = useUser();

  const checkValidNickname = (e) => {
    const includeSpecialCharacter = new RegExp(
      PATTERNS.SPECIAL_CHARACTERS_WITHOUT_SPACE
    );

    if (includeSpecialCharacter.test(e.target.value)) {
      setIsValidNickname(false);
      setErrorMessage('한글, 영어, 숫자, 공백만 포함 가능합니다.');

      return;
    }

    setIsValidNickname(true);
  };

  const submitNickname = async (e) => {
    e.preventDefault();
    const nicknameInput = e.target.nickname.value;

    try {
      const response = await axios.post(`${BASE_URL}/api/users`, {
        nickname: nicknameInput,
      });
      const generatedUser = response.data;

      setSessionStorage('nickname', generatedUser.nickname);

      setIsNicknameChanged(true);
      changeUser({ id: generatedUser.id, nickname: generatedUser.nickname });
      closeModal();
    } catch (error) {
      //TODO: 상수화 필요
      const [{ message }] = error.response.data;
      const [splittedMessage] = message.split(' 입력 닉네임:');
      const [, errorMessage] = splittedMessage.split('닉네임은 ');

      setErrorMessage(errorMessage);
    }
  };

  return (
    <form className='nickname-container' onSubmit={submitNickname}>
      <div onClick={closeModal} className='control-bar'>
        <IoCloseOutline size='24px' />
      </div>
      <div className='control-inputs'>
        <Subtitle3>닉네임 변경하기</Subtitle3>
        <TextInput
          defaultValue={user.nickname}
          name='nickname'
          placeholder='닉네임을 입력해주세요.'
          maxLength={NICKNAME_MAX_LENGTH}
          minLength={NICKNAME_MIN_LENGTH}
          onChangeInput={checkValidNickname}
          isContentSelected
          autocomplete='off'
        />

        <span className={`form ${isValidNickname ? 'confirm' : 'error'}`}>
          {isValidNickname ? '옳은 입력입니다.' : errorMessage}
        </span>
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
