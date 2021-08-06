import './ModalError.scss';

import { Body2, Caption1, Subtitle2 } from '../../core/Typography';

import PropTypes from 'prop-types';
import React from 'react';
import { RoundButton } from '../../components';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const ModalError = ({ children }) => {
  const { closeModal } = useDefaultModal();

  return (
    <div className='error-container'>
      <Subtitle2>ERROR</Subtitle2>
      <Body2>{children}</Body2>
      <RoundButton size='small' onClick={closeModal} colored>
        <Caption1>확인하기</Caption1>
      </RoundButton>
    </div>
  );
};

ModalError.propTypes = {
  children: PropTypes.node,
};

export default ModalError;
