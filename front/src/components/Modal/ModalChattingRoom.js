import './Modal.scss';

import React, { useEffect } from 'react';

import Chatbox from '../../chunks/Chatbox/Chatbox';
import { IoCloseOutline } from 'react-icons/io5';
import { IoRemove } from 'react-icons/io5';
import LinearLayout from '../../core/Layout/LinearLayout';
import Participants from '../../chunks/Participants/Participants';
import PropTypes from 'prop-types';
import Subtitle3 from '../../core/Typography/Subtitle3';
import TagList from '../../chunks/TagList/TagList';
import { useModal } from '../../contexts/ModalProvider';

const ModalChattingRoom = ({ tags, participants, roomNo, createdAt }) => {
  const { close, minimize } = useModal();

  const onMinimize = () => minimize();

  const onClose = () => close();

  useEffect(() => {
    // 웹소켓 연결, 웹소켓으로 subscribe해서 방 참가자 정보 불러오기
  }, []);

  return (
    <div className='modal-chatting-room'>
      <div className='modal-header-container'>
        <LinearLayout direction='row'>
          <LinearLayout direction='row'>
            <Subtitle3>태그</Subtitle3>
            <TagList tags={tags} />
          </LinearLayout>
          <LinearLayout direction='row'>
            <button className='room-minimize' onClick={onMinimize}>
              <IoRemove size='24px' />
            </button>
            <button className='room-exit' onClick={onClose}>
              <IoCloseOutline size='24px' />
            </button>
          </LinearLayout>
        </LinearLayout>
      </div>
      <div className='modal-body-container'>
        <div className='modal-aside-container'>
          <Participants participants={participants} />
        </div>
        <div className='modal-chatbox-container'>
          <Chatbox roomNo={roomNo} createdAt={createdAt}></Chatbox>
        </div>
      </div>
    </div>
  );
};

ModalChattingRoom.propTypes = {
  tags: PropTypes.arrayOf(PropTypes.string),
  participants: PropTypes.shape({
    host: PropTypes.shape({
      id: PropTypes.number,
      name: PropTypes.string,
      profileImg: PropTypes.string,
    }),
    guests: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.number,
        name: PropTypes.string,
        profileImg: PropTypes.string,
      })
    ),
  }),
  roomNo: PropTypes.number,
  createdAt: PropTypes.string,
};

export default ModalChattingRoom;
