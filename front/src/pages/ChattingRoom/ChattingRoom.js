import './ChattingRoom.scss';

import React, { useEffect, useRef, useState } from 'react';

import Chatbox from '../../chunks/Chatbox/Chatbox';
import { IoCloseOutline } from 'react-icons/io5';
import { IoRemove } from 'react-icons/io5';
import LinearLayout from '../../core/Layout/LinearLayout';
import Participants from '../../chunks/Participants/Participants';
import PropTypes from 'prop-types';
import SockJS from 'sockjs-client';
import SpeechBubble from '../../components/SpeechBubble/SpeechBubble';
import SpeechBubbleWithAvatar from '../../components/SpeechBubble/SpeechBubbleWithAvatar';
import { Stomp } from '@stomp/stompjs';
import Subtitle3 from '../../core/Typography/Subtitle3';
import TagList from '../../chunks/TagList/TagList';
import { useModal } from '../../contexts/ModalProvider';

const ChattingRoom = ({ tags, participants, roomId, createdAt }) => {
  const [chattings, setChattings] = useState([]);
  const stompClient = useRef(null);
  const user_subscription = useRef(null);
  const chat_subscription = useRef(null);
  const userId = useRef(-1);
  const { open, close, minimize } = useModal();

  const onMinimize = () => minimize();

  const onClose = () => close();

  const onSubmit = (e) => {
    e.preventDefault();

    const content = e.target.chat.value;
    if (!content) return;

    stompClient.current.send(
      `/ws/rooms/${roomId}/chat`,
      {},
      JSON.stringify({ userId: userId.current, content })
    );

    e.target.reset();
  };

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

      user_subscription.current = stompClient.current.subscribe(
        `/topic/rooms/${roomId}/users`,
        (message) => {
          const users = JSON.parse(message.body);
          open(
            <ChattingRoom
              tags={tags}
              participants={users}
              roomId={roomId}
              createdAt={createdAt}
            />,
            'chatting'
          );
        }
      );

      chat_subscription.current = stompClient.current.subscribe(
        `/topic/rooms/${roomId}/chat`,
        (message) => {
          setChattings((prevChattings) => [
            ...prevChattings,
            {
              user: JSON.parse(message.body).user,
              content: JSON.parse(message.body).content,
            },
          ]);
        }
      );

      userId.current = Number(prompt('유저 아이디 입력하소'));
      stompClient.current.send(
        `/ws/rooms/${roomId}/users`,
        {},
        JSON.stringify({ userId: userId.current, sessionId })
      );
    });

    return () => {
      if (user_subscription.current) {
        user_subscription.current.unsubscribe();
      }
      if (chat_subscription.current) {
        chat_subscription.current.unsubscribe();
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
            <TagList tags={tags} useWheel={true} />
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
          <Chatbox roomId={roomId} createdAt={createdAt} onSubmit={onSubmit}>
            {chattings &&
              chattings.map((chatting, index) =>
                chatting.user.id === userId.current ? (
                  <SpeechBubble key={index}>{chatting.content}</SpeechBubble>
                ) : (
                  <SpeechBubbleWithAvatar
                    key={index}
                    nickname={chatting.user.name}
                  >
                    {chatting.content}
                  </SpeechBubbleWithAvatar>
                )
              )}
          </Chatbox>
        </div>
      </div>
    </div>
  );
};

ChattingRoom.propTypes = {
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

export default ChattingRoom;
