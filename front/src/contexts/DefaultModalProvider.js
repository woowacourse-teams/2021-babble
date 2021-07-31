import React, { createContext, useContext, useState } from 'react';

import { MODAL_TYPE_DEFAULT } from '../constants/chat';
import Modal from '../components/Modal/Modal';
import PropTypes from 'prop-types';

const DefaultModalContext = createContext();

const DefaultModalProvider = ({ children }) => {
  const [isDefaultModalOpen, setIsDefaultModalOpen] = useState(false);
  const [modalInner, setModalInner] = useState(null);

  const open = (modalInner) => {
    setIsDefaultModalOpen(true);
    setModalInner(modalInner);
  };

  const close = () => {
    setIsDefaultModalOpen(false);
    setModalInner(null);
  };

  return (
    <DefaultModalContext.Provider value={{ open, close }}>
      {children}
      {isDefaultModalOpen && (
        <Modal type={MODAL_TYPE_DEFAULT}>{modalInner}</Modal>
      )}
    </DefaultModalContext.Provider>
  );
};

DefaultModalProvider.propTypes = {
  children: PropTypes.node,
};

export const useDefaultModal = () => {
  return useContext(DefaultModalContext);
};

export default DefaultModalProvider;
