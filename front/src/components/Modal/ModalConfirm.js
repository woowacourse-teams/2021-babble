import './ModalCustom.scss';

import { Body2, Caption1, Subtitle2 } from '../../core/Typography';

import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import React from 'react';
import { RoundButton } from '../../components';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const ModalConfirm = ({ confirmCallback, children }) => {
  const { closeModal } = useDefaultModal();

  const confirm = () => {
    confirmCallback?.();
    closeModal();
  };

  return (
    <div className='confirm-container'>
      <Subtitle2>확인하기</Subtitle2>
      <Body2>{children}</Body2>
      <LinearLayout direction='row'>
        <RoundButton size='small' onClick={closeModal}>
          <Caption1>취소하기</Caption1>
        </RoundButton>
        <RoundButton size='small' onClick={confirm} colored>
          <Caption1>확인하기</Caption1>
        </RoundButton>
      </LinearLayout>
    </div>
  );
};

ModalConfirm.propTypes = {
  confirmCallback: PropTypes.func,
  children: PropTypes.node,
};

export default ModalConfirm;
