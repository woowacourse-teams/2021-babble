import './Modal.scss';

import React, { useEffect, useRef } from 'react';

import Chatbox from '../../chunks/Chatbox/Chatbox';
import { IoCloseOutline } from 'react-icons/io5';
import { IoRemove } from 'react-icons/io5';
import LinearLayout from '../../core/Layout/LinearLayout';
import Participants from '../../chunks/Participants/Participants';
import PropTypes from 'prop-types';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import Subtitle3 from '../../core/Typography/Subtitle3';
import TagList from '../../chunks/TagList/TagList';
import { useModal } from '../../contexts/ModalProvider';

const ModalChattingRoom = ({ tags, participants, roomId, createdAt }) => {
  const stompClient = useRef(null);
  const subscription = useRef(null);
  const userId = useRef(-1);
  const { open, close, minimize } = useModal();

  const onMinimize = () => minimize();

  const onClose = () => close();

  useEffect(() => {
    // 웹소켓 연결, 웹소켓으로 subscribe해서 방 참가자 정보 불러오기
    const socket = new SockJS('https://babble-test.o-r.kr/connection');
    stompClient.current = Stomp.over(socket);

    stompClient.current.connect({}, () => {
      const socketURL = socket._transport.url;
      const sessionId = socketURL.substring(
        socketURL.lastIndexOf('/') - 8,
        socketURL.lastIndexOf('/')
      );

      subscription.current = stompClient.current.subscribe(
        `/topic/rooms/${roomId}/users`,
        (message) => {
          const users = JSON.parse(message.body);
          open(
            <ModalChattingRoom
              tags={tags}
              participants={users}
              roomId={roomId}
              createdAt={createdAt}
            />
          );
        }
      );

      userId.current = Number(prompt('유저 아이디 입력하소'));
      stompClient.current.send(
        `/ws/rooms/${roomId}/users`,
        {},
        JSON.stringify({ userId, sessionId })
      );
    });

    return () => {
      if (subscription.current) {
        subscription.current.unsubscribe();
      }
      if (stompClient.current) {
        stompClient.current.disconnect();
      }
    };
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
          <Chatbox roomId={roomId} createdAt={createdAt}></Chatbox>
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
  roomId: PropTypes.number,
  createdAt: PropTypes.string,
};

export default ModalChattingRoom;
