import './Modal.scss';

import { IoCloseOutline } from 'react-icons/io5';
import PropTypes from 'prop-types';
import React from 'react';
import ReactDOM from 'react-dom';
import { VscChevronUp } from 'react-icons/vsc';

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

  return ReactDOM.createPortal(
    content,
    document.querySelector('#chatting-modal')
  );
};

ModalMinimized.propTypes = {
  maximize: PropTypes.func,
  close: PropTypes.func,
};

export default ModalMinimized;
