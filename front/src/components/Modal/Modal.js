import './Modal.scss';

import React, { forwardRef } from 'react';

import LinearLayout from '../../core/Layout/LinearLayout';
import { MODAL_TYPE_DEFAULT } from '../../constants/chat';
import PropTypes from 'prop-types';
import ReactDOM from 'react-dom';

const Modal = forwardRef(
  (
    { type = MODAL_TYPE_DEFAULT, isMinimized, onEscapeKeyDown, children },
    ref
  ) => {
    const content = (
      <div
        ref={ref}
        className={`modal-container ${type} ${isMinimized ? 'minimized' : ''}`}
        onKeyDown={onEscapeKeyDown}
      >
        <LinearLayout>{children}</LinearLayout>
      </div>
    );

    return ReactDOM.createPortal(content, document.querySelector('#modal'));
  }
);

Modal.displayName = 'Modal';

Modal.propTypes = {
  type: PropTypes.oneOf(['default', 'chatting']),
  isMinimized: PropTypes.bool,
  onEscapeKeyDown: PropTypes.func,
  children: PropTypes.node,
};

export default Modal;
