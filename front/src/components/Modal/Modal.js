import './Modal.scss';

import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import React from 'react';
import ReactDOM from 'react-dom';

const Modal = ({ type = 'default', isMinimized, children }) => {
  const content = (
    <div
      className={`modal-container ${type} ${isMinimized ? 'minimized' : ''}`}
    >
      <LinearLayout>{children}</LinearLayout>
    </div>
  );

  return ReactDOM.createPortal(content, document.querySelector('#modal'));
};

Modal.propTypes = {
  type: PropTypes.oneOf(['default', 'chatting']),
  isMinimized: PropTypes.bool,
  children: PropTypes.node,
};

export default Modal;
