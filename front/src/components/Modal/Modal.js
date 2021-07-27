import './Modal.scss';

import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import React from 'react';
import ReactDOM from 'react-dom';

const Modal = ({ type = 'default', children }) => {
  const content = (
    <div className={`modal-container ${type}`}>
      <LinearLayout>{children}</LinearLayout>
    </div>
  );

  return ReactDOM.createPortal(content, document.querySelector('#modal'));
};

Modal.propTypes = {
  type: PropTypes.oneOf(['default', 'chatting']),
  children: PropTypes.node,
};

export default Modal;
