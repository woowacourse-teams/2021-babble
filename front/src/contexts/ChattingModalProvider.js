import { Modal, ModalMinimized } from '../components';
import React, { createContext, useContext, useState } from 'react';

import { MODAL_TYPE_CHATTING } from '../constants/chat';
import PropTypes from 'prop-types';

const ChattingModalContext = createContext();

const ChattingModalProvider = ({ children }) => {
  const [isChattingModalOpen, setIsChattingModalOpen] = useState(false);
  const [isMinimized, setIsMinimized] = useState(false);
  const [modalInner, setModalInner] = useState(null);

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

  // TODO: 데모데이 이후 isChattingModalOpen 없애기
  return (
    <ChattingModalContext.Provider
      value={{ openChatting, closeChatting, minimize, isChattingModalOpen }}
    >
      {children}
      {isChattingModalOpen && (
        <Modal type={MODAL_TYPE_CHATTING} isMinimized={isMinimized}>
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
