import React, { createContext, useContext, useState } from 'react';

import { MODAL_TYPE_DEFAULT } from '../constants/chat';
import { Modal } from '../components';
import ModalConfirm from '../components/Modal/ModalConfirm';
import PropTypes from 'prop-types';

const DefaultModalContext = createContext();

const DefaultModalProvider = ({ children }) => {
  const [isDefaultModalOpen, setIsDefaultModalOpen] = useState(false);
  const [modalInner, setModalInner] = useState(null);

  const openModal = (modalInner) => {
    setIsDefaultModalOpen(true);
    setModalInner(modalInner);
  };

  const closeModal = () => {
    setIsDefaultModalOpen(false);
    setModalInner(null);
  };

  return (
    <DefaultModalContext.Provider value={{ openModal, closeModal }}>
      {children}
      {isDefaultModalOpen && (
        <Modal type={MODAL_TYPE_DEFAULT}>
          <div className='default-container'>{modalInner}</div>
        </Modal>
      )}
    </DefaultModalContext.Provider>
  );
};

DefaultModalProvider.propTypes = {
  children: PropTypes.node,
};

export const useDefaultModal = () => {
  const { openModal, closeModal } = useContext(DefaultModalContext);

  const onConfirm = (callback, condition, statement) => {
    if (condition) {
      openModal(
        <ModalConfirm confirmCallback={callback}>{statement}</ModalConfirm>
      );
      return;
    }

    callback();
  };

  return { openModal, closeModal, onConfirm };
};

export default DefaultModalProvider;
