import './ModalError.scss';

import Body2 from '../../core/Typography/Body2';
import Caption1 from '../../core/Typography/Caption1';
import PropTypes from 'prop-types';
import React from 'react';
import RoundButton from '../../components/Button/RoundButton';
import Subtitle2 from '../../core/Typography/Subtitle2';
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
