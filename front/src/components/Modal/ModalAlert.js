import './ModalCustom.scss';

import { Body2, Caption1, Subtitle2 } from '../../core/Typography';

import PropTypes from 'prop-types';
import React from 'react';
import { RoundButton } from '../../components';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const ModalAlert = ({ children }) => {
  const { closeModal } = useDefaultModal();

  return (
    <div className='alert-container'>
      <Subtitle2>안내</Subtitle2>
      <Body2>{children}</Body2>
      <RoundButton size='small' onClickButton={closeModal} colored>
        <Caption1>확인하기</Caption1>
      </RoundButton>
    </div>
  );
};

ModalAlert.propTypes = {
  children: PropTypes.node,
};

export default ModalAlert;
