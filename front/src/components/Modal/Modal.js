import './Modal.scss';

import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import React from 'react';
import ReactDOM from 'react-dom';

const Modal = ({ children }) => {
  const content = (
    <div className='modal-container'>
      <LinearLayout>{children}</LinearLayout>
    </div>
  );

  return ReactDOM.createPortal(
    content,
    document.querySelector('#chatting-modal')
  );
};

Modal.propTypes = {
  children: PropTypes.node,
};

export default Modal;
