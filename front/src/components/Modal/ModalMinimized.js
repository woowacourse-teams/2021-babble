import './Modal.scss';

import { IoCloseOutline } from '@react-icons/all-files/io5/IoCloseOutline';
import PropTypes from 'prop-types';
import React from 'react';
import ReactDOM from 'react-dom';
import { VscChevronUp } from '@react-icons/all-files/vsc/VscChevronUp';

const ModalMinimized = ({ maximize, close }) => {
  const content = (
    <div className='modal-minimized-container' onClick={maximize}>
      <section className='modal-button-container'>
        <span className='maximize'>
          <VscChevronUp size='24px' />
        </span>
        <span className='close' onClick={close}>
          <IoCloseOutline size='24px' />
        </span>
      </section>
    </div>
  );

  return ReactDOM.createPortal(content, document.querySelector('#modal'));
};

ModalMinimized.propTypes = {
  maximize: PropTypes.func,
  close: PropTypes.func,
};

export default ModalMinimized;
