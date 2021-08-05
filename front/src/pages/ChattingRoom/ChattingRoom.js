import './ChattingRoom.scss';

import {
  ModalError,
  SpeechBubble,
  SpeechBubbleWithAvatar,
} from '../../components';
import React, { useEffect, useRef, useState } from 'react';
import { SESSION_ID_LENGTH, SOCKET_URL_DIVIDER } from '../../constants/chat';

import Chatbox from '../../chunks/Chatbox/Chatbox';
import { IoCloseOutline } from 'react-icons/io5';
import { IoRemove } from 'react-icons/io5';
import LinearLayout from '../../core/Layout/LinearLayout';
import Participants from '../../chunks/Participants/Participants';
import PropTypes from 'prop-types';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { Subtitle3 } from '../../core/Typography';
import TagList from '../../chunks/TagList/TagList';
import axios from 'axios';
import { useChattingModal } from '../../contexts/ChattingModalProvider';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';
import useUpdateEffect from '../../hooks/useUpdateEffect';
import { useUser } from '../../contexts/UserProvider';

const ChattingRoom = ({ tags, roomId, createdAt }) => {
  const [chattings, setChattings] = useState([]);
  const [participants, setParticipants] = useState({});

  const stompClient = useRef(null);
  const user_subscription = useRef(null);
  const chat_subscription = useRef(null);
  const isConnected = useRef(false);

  const { openModal } = useDefaultModal();
  const { closeChatting, minimize } = useChattingModal();
  const {
    user: { nickname, id: userId },
    changeUserId,
  } = useUser();

  const waitForConnectionReady = (callback) => {
    setTimeout(() => {
      if (stompClient.current.ws.readyState === WebSocket.OPEN) {
        callback();
      } else {
        waitForConnectionReady(callback);
      }
    }, 1);
  };

  const sendMessage = (content) => {
    waitForConnectionReady(() => {
      stompClient.current.send(
        `/ws/rooms/${roomId}/chat`,
        {},
        JSON.stringify({ userId, content })
      );
    });
  };

  const onSubmit = (e) => {
    e.preventDefault();

    const content = e.target.chat.value;
    if (!content) return;

    sendMessage(content);
    e.target.reset();
  };

  const getUserId = async () => {
    const response = await axios.post('https://babble-test.o-r.kr/api/users', {
      nickname,
    });
    const generatedUser = response.data;

    changeUserId(generatedUser.id);
  };

  useEffect(() => {
    getUserId();

    return () => {
      if (isConnected.current) {
        stompClient.current.disconnect(() => {
          if (user_subscription.current) {
            user_subscription.current.unsubscribe();
          }
          if (chat_subscription.current) {
            chat_subscription.current.unsubscribe();
          }
        });
        isConnected.current = false;
      }
    };
  }, []);

  useUpdateEffect(() => {
    const socket = new SockJS('https://babble-test.o-r.kr/connection');
    stompClient.current = Stomp.over(socket);

    if (isConnected.current) {
      stompClient.current.disconnect();
      isConnected.current = false;
    }

    stompClient.current.connect(
      {},
      () => {
        isConnected.current = true;
        const socketURL = socket._transport.url;
        const sessionId = socketURL.substring(
          socketURL.lastIndexOf(SOCKET_URL_DIVIDER) - SESSION_ID_LENGTH,
          socketURL.lastIndexOf(SOCKET_URL_DIVIDER)
        );

        user_subscription.current = stompClient.current.subscribe(
          `/topic/rooms/${roomId}/users`,
          (message) => {
            const users = JSON.parse(message.body);
            setParticipants(users);
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

        waitForConnectionReady(() => {
          stompClient.current.send(
            `/ws/rooms/${roomId}/users`,
            {},
            JSON.stringify({ userId, sessionId })
          );
        });

        sendMessage(`${nickname}님이 입장했습니다!`);
      },
      (error) => {
        const errorStrings = error.headers.message.split(':');
        const errorMessage = errorStrings[errorStrings.length - 1].trim();

        closeChatting();
        openModal(<ModalError>{errorMessage}</ModalError>);
      }
    );
  }, [userId]);

  return (
    <div className='modal-chatting-room'>
      <div className='modal-header-container'>
        <LinearLayout direction='row'>
          <LinearLayout direction='row'>
            <Subtitle3>태그</Subtitle3>
            <TagList tags={tags} useWheel={true} />
          </LinearLayout>
          <LinearLayout direction='row'>
            <button className='room-minimize' onClick={minimize}>
              <IoRemove size='24px' />
            </button>
            <button className='room-exit' onClick={closeChatting}>
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
            {chattings.map((chatting, index) =>
              chatting.user.id === userId ? (
                <SpeechBubble key={index}>{chatting.content}</SpeechBubble>
              ) : (
                <SpeechBubbleWithAvatar
                  key={index}
                  nickname={chatting.user.nickname}
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
  roomId: PropTypes.number,
  createdAt: PropTypes.string,
};

export default ChattingRoom;
