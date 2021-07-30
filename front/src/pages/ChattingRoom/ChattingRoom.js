import './ChattingRoom.scss';

import {
  MODAL_TYPE_CHATTING,
  SESSION_ID_LENGTH,
  SOCKET_URL_DIVIDER,
} from '../../constants/chat';
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
import axios from 'axios';
import { useModal } from '../../contexts/ModalProvider';
import { useUser } from '../../contexts/UserProvider';

const ChattingRoom = ({ tags, participants, roomId, createdAt }) => {
  const [chattings, setChattings] = useState([]);
  const stompClient = useRef(null);
  const user_subscription = useRef(null);
  const chat_subscription = useRef(null);
  const { open, close, minimize } = useModal();
  const {
    user: { nickname, id: userId },
    changeUserId,
  } = useUser();

  const onMinimize = () => minimize();

  const onClose = () => close();

  const onSubmit = (e) => {
    e.preventDefault();

    const content = e.target.chat.value;
    if (!content) return;

    stompClient.current.send(
      `/ws/rooms/${roomId}/chat`,
      {},
      JSON.stringify({ userId, content })
    );

    e.target.reset();
  };

  // 닉네임 변경 안하고 그냥 방 목록에 있는 방을 선택한 상황: default nickname으로 userId 신청
  // userId 발급받고 방 입장(connection)

  const getUserId = async () => {
    const generatedUser = await axios.post(
      'https://babble-test.o-r.kr/api/users',
      {
        nickname,
      }
    );

    changeUserId(generatedUser.id);
  };

  // useEffect(() => {
  //   getUserId();
  // }, []);

  useEffect(() => {
    const socket = new SockJS('https://babble-test.o-r.kr/connection');
    stompClient.current = Stomp.over(socket);

    getUserId();

    stompClient.current.connect({}, () => {
      const socketURL = socket._transport.url;
      const sessionId = socketURL.substring(
        socketURL.lastIndexOf(SOCKET_URL_DIVIDER) - SESSION_ID_LENGTH,
        socketURL.lastIndexOf(SOCKET_URL_DIVIDER)
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
            MODAL_TYPE_CHATTING
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

      stompClient.current.send(
        `/ws/rooms/${roomId}/users`,
        {},
        JSON.stringify({ userId, sessionId })
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
                chatting.user.id === userId ? (
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
  tags: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number,
      name: PropTypes.string,
    })
  ),
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
