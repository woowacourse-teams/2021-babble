import { Modal, ModalMinimized } from '../components';
import React, { createContext, useContext, useRef, useState } from 'react';

import { MODAL_TYPE_CHATTING } from '../constants/chat';
import PropTypes from 'prop-types';
import useUpdateEffect from '../hooks/useUpdateEffect';

const ChattingModalContext = createContext();

const ChattingModalProvider = ({ children }) => {
  const [isChattingModalOpen, setIsChattingModalOpen] = useState(false);
  const [isMinimized, setIsMinimized] = useState(false);
  const [modalInner, setModalInner] = useState(null);
  const modalRef = useRef(null);

  const openChatting = (modalInner) => {
    setIsChattingModalOpen(true);
    setModalInner(modalInner);
  };

  const closeChatting = () => {
    setIsChattingModalOpen(false);
    setIsMinimized(false);
    setModalInner(null);
  };

  const clickCloseChatting = (e) => {
    if (isMinimized) {
      e.stopPropagation();
    }

    setIsChattingModalOpen(false);
    setIsMinimized(false);
    setModalInner(null);
  };

  const minimize = () => {
    setIsMinimized(true);
  };

  const maximize = () => {
    setIsMinimized(false);
  };

  const onEscapeKeyDown = (e) => {
    if (isChattingModalOpen && e.key === 'Escape') {
      setIsMinimized(true);
    }
  };

  useUpdateEffect(() => {
    if (isChattingModalOpen && !isMinimized) {
      modalRef.current.querySelector('textarea').focus();
    }
  }, [isMinimized]);

  // TODO: 데모데이 이후 isChattingModalOpen 없애기
  return (
    <ChattingModalContext.Provider
      value={{ openChatting, closeChatting, minimize, isChattingModalOpen }}
    >
      {children}
      {isChattingModalOpen && (
        <Modal
          type={MODAL_TYPE_CHATTING}
          isMinimized={isMinimized}
          onEscapeKeyDown={onEscapeKeyDown}
          ref={modalRef}
        >
          {modalInner}
        </Modal>
      )}
      {isMinimized && (
        <ModalMinimized maximize={maximize} close={clickCloseChatting} />
      )}
    </ChattingModalContext.Provider>
  );
};

ChattingModalProvider.propTypes = {
  children: PropTypes.node,
};

export const useChattingModal = () => {
  return useContext(ChattingModalContext);
};

export default ChattingModalProvider;
