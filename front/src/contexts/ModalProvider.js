import React, { createContext, useContext, useState } from 'react';

import Modal from '../components/Modal/Modal';
import ModalMinimized from '../components/Modal/ModalMinimized';
import PropTypes from 'prop-types';

const ModalContext = createContext();

const ModalProvider = ({ children }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [isMinimized, setIsMinimized] = useState(false);
  const [modalInner, setModalInner] = useState(null);

  const open = (modalInner) => {
    setIsOpen(true);
    setModalInner(modalInner);
  };

  const close = (e) => {
    if (isMinimized) {
      e.stopPropagation();
    }

    setIsOpen(false);
    setIsMinimized(false);
    setModalInner(null);
  };

  const minimize = () => {
    setIsOpen(false);
    setIsMinimized(true);
  };

  const maximize = () => {
    setIsOpen(true);
    setIsMinimized(false);
  };

  return (
    <ModalContext.Provider value={{ open, close, minimize }}>
      {children}
      {isOpen && <Modal>{modalInner}</Modal>}
      {isMinimized && <ModalMinimized maximize={maximize} close={close} />}
    </ModalContext.Provider>
  );
};

ModalProvider.propTypes = {
  children: PropTypes.node,
};

export const useModal = () => {
  return useContext(ModalContext);
};

export default ModalProvider;
