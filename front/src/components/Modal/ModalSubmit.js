import './ModalCustom.scss';

import { Caption1, Subtitle3 } from '../../core/Typography';
import { RoundButton, TextInput } from '..';

import { IoCloseOutline } from '@react-icons/all-files/io5/IoCloseOutline';
import PropTypes from 'prop-types';
import React from 'react';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const ModalSubmit = ({
  title,
  inputPlaceholder,
  onSubmit,
  isSecret = false,
}) => {
  const { closeModal } = useDefaultModal();

  return (
    <form className='submit-container' onSubmit={onSubmit}>
      <div onClick={closeModal} className='control-bar'>
        <IoCloseOutline size='24px' />
      </div>
      <div className='control-inputs'>
        <Subtitle3>{title}</Subtitle3>
        <TextInput
          type={isSecret ? 'password' : 'text'}
          name='password'
          placeholder={inputPlaceholder}
          isContentSelected
          autocomplete='off'
        />
      </div>
      <div className='control-buttons'>
        <RoundButton onClickButton={closeModal} size='small'>
          <Caption1>취소하기</Caption1>
        </RoundButton>
        <RoundButton type='submit' size='small' colored>
          <Caption1>확인하기</Caption1>
        </RoundButton>
      </div>
    </form>
  );
};

ModalSubmit.propTypes = {
  title: PropTypes.string,
  inputPlaceholder: PropTypes.string,
  onSubmit: PropTypes.func,
  isSecret: PropTypes.bool,
};

export default ModalSubmit;
