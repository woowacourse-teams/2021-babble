import './ChattingRoom.scss';

import { CONNECTION_URL, SEND_URL, SUBSCRIBE_URL } from '../../constants/api';
import {
  ModalError,
  SpeechBubble,
  SpeechBubbleWithAvatar,
  ToggleNotification,
} from '../../components';
import React, { useEffect, useRef, useState } from 'react';
import { SESSION_ID_LENGTH, SOCKET_URL_DIVIDER } from '../../constants/chat';

import Chatbox from '../../chunks/Chatbox/Chatbox';
import { FaChevronDown } from '@react-icons/all-files/fa/FaChevronDown';
import { FaChevronUp } from '@react-icons/all-files/fa/FaChevronUp';
import { FaUsers } from '@react-icons/all-files/fa/FaUsers';
import { IoCloseOutline } from '@react-icons/all-files/io5/IoCloseOutline';
import { IoRemove } from '@react-icons/all-files/io5/IoRemove';
import KakaoShareButton from '../../components/KakaoShareButton/KakaoShareButton';
import LinearLayout from '../../core/Layout/LinearLayout';
import Participants from '../../chunks/Participants/Participants';
import PropTypes from 'prop-types';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { Subtitle3 } from '../../core/Typography';
import TagList from '../../chunks/TagList/TagList';
import { useChattingModal } from '../../contexts/ChattingModalProvider';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';
import { usePushNotification } from '../../contexts/PushNotificationProvider';
import useTabNotification from '../../hooks/useTabNotification';
import { useUser } from '../../contexts/UserProvider';

const ChattingRoom = ({ tags, game, roomId }) => {
  const [chattings, setChattings] = useState([]);
  const [participants, setParticipants] = useState({});
  const [isTagsVisible, setIsTagsVisible] = useState(false);
  const [isParticipantsVisible, setIsParticipantsVisible] = useState(false);

  const stompClient = useRef(null);
  const user_subscription = useRef(null);
  const chat_subscription = useRef(null);
  const isConnected = useRef(false);

  const { openModal } = useDefaultModal();
  const { closeChatting, minimize } = useChattingModal();
  const {
    user: { nickname, id: userId },
    changeCurrentRoomNumber,
  } = useUser();

  const { fireNotificationWithTimeout, isNotificationOn } =
    usePushNotification();
  const { showNotificationCount } = useTabNotification();
  const isNotificationOnRef = useRef(isNotificationOn);

  useEffect(() => {
    isNotificationOnRef.current = isNotificationOn;
  }, [isNotificationOn]);

  // mobile
  const toggleParticipants = () => {
    setIsParticipantsVisible(
      (wasParticipantsVisible) => !wasParticipantsVisible
    );
  };

  const toggleTags = () => {
    setIsTagsVisible((wasTagsVisible) => !wasTagsVisible);
  };

  const waitForConnectionReady = (callback) => {
    setTimeout(() => {
      if (stompClient.current.ws.readyState === WebSocket.OPEN) {
        callback();
      } else {
        waitForConnectionReady(callback);
      }
    }, 1);
  };

  const sendMessage = (content, type) => {
    waitForConnectionReady(() => {
      stompClient.current.send(
        SEND_URL.CHAT(roomId),
        {},
        JSON.stringify({ userId, content, type })
      );
    });
  };

  const onSubmit = (e) => {
    e.preventDefault();

    const content = e.target.chat.value;
    if (!content) return;

    sendMessage(content, 'chat');
    e.currentTarget.reset();
    e.currentTarget.chat.focus();
  };

  useEffect(() => {
    const socket = new SockJS(CONNECTION_URL);
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
          SUBSCRIBE_URL.USERS(roomId),
          (message) => {
            const users = JSON.parse(message.body);
            setParticipants(users);
          }
        );

        chat_subscription.current = stompClient.current.subscribe(
          SUBSCRIBE_URL.CHAT(roomId),
          (message) => {
            const receivedTime = new Date().toLocaleTimeString([], {
              timeStyle: 'short',
            });

            const user = JSON.parse(message.body).user;
            const content = JSON.parse(message.body).content;
            const type = JSON.parse(message.body).type;

            if (
              user.nickname !== nickname &&
              (document.hidden || !document.hasFocus()) &&
              type === 'chat'
            ) {
              if (isNotificationOnRef.current) {
                fireNotificationWithTimeout('Babble 채팅 메시지', 3500, {
                  body: `${user.nickname}: ${content}`,
                });
              }

              showNotificationCount({ blinkMessage: 'New Message!' });
            }

            setChattings((prevChattings) => [
              ...prevChattings,
              { user, content, type, receivedTime },
            ]);
          }
        );

        waitForConnectionReady(() => {
          stompClient.current.send(
            SEND_URL.USERS(roomId),
            {},
            JSON.stringify({ userId, sessionId })
          );
        });

        sendMessage(`${nickname}님이 입장했습니다!`, 'notice');

        changeCurrentRoomNumber(roomId);
      },
      (error) => {
        const errorStrings = error.headers.message.split(':');
        const errorMessage = errorStrings[errorStrings.length - 1].trim();

        closeChatting();
        openModal(<ModalError>{errorMessage}</ModalError>);
      }
    );

    return () => {
      if (isConnected.current) {
        stompClient.current.disconnect(() => {
          if (user_subscription.current) {
            user_subscription.current.unsubscribe();
          }
          if (chat_subscription.current) {
            chat_subscription.current.unsubscribe();
          }
          isConnected.current = false;

          changeCurrentRoomNumber(-1);
        });
      }
    };
  }, []);

  useEffect(() => {
    const prevChat = chattings[chattings.length - 2];
    const currentChat = chattings[chattings.length - 1];
    console.log(chattings);

    if (prevChat) {
      if (
        prevChat.receivedTime === currentChat.receivedTime &&
        prevChat.user.id === currentChat.user.id &&
        prevChat.type === 'chat'
      ) {
        setChattings((prevChattings) =>
          prevChattings.map((chat, index) => {
            const lastElementIndex = prevChattings.length - 1;
            const secondLastElementIndex = prevChattings.length - 2;
            if (
              index === lastElementIndex &&
              prevChattings[index - 1].user.id === prevChattings[index].user.id
            ) {
              return {
                ...chat,
                user: { ...chat.user, avatar: null, nickname: null },
              };
            }

            if (index === secondLastElementIndex) {
              return { ...chat, receivedTime: null };
            }

            return chat;
          })
        );
      }
    }
  }, [chattings]);

  return (
    <div className='modal-chatting-room'>
      <div className={`modal-header-container ${isTagsVisible ? 'open' : ''}`}>
        <div className='room-info'>
          <div className='room-nav'>
            <LinearLayout direction='row'>
              <Subtitle3>
                {roomId}
                <span className='room-number-text'>번 방</span>
              </Subtitle3>
              <Subtitle3>{game.name}</Subtitle3>
              <KakaoShareButton gameId={game.id} roomId={roomId} />
            </LinearLayout>
            <LinearLayout direction='row'>
              <button className='room-minimize' onClick={minimize}>
                <IoRemove size='24px' />
              </button>
              <button className='room-exit' onClick={closeChatting}>
                <IoCloseOutline size='24px' />
              </button>
            </LinearLayout>
          </div>
          <div className={`tags ${isTagsVisible ? 'show' : ''}`}>
            <TagList tags={tags} />
          </div>
          <button className='show-tags' onClick={toggleTags}>
            {isTagsVisible ? <FaChevronUp /> : <FaChevronDown />}
          </button>
        </div>
      </div>
      <div className='modal-body-container'>
        <button
          className={`show-participants ${isParticipantsVisible ? 'show' : ''}`}
          onClick={toggleParticipants}
        >
          {isParticipantsVisible ? (
            <IoCloseOutline size='26px' />
          ) : (
            <FaUsers size='26px' />
          )}
        </button>
        <div
          className={`modal-aside-container ${
            isParticipantsVisible ? 'show' : ''
          }`}
        >
          <Participants participants={participants} />
          <ToggleNotification />
        </div>

        <div className='modal-chatbox-container'>
          <Chatbox onSubmit={onSubmit}>
            {chattings.map((chatting, index) => {
              if (chatting.type === 'chat') {
                return chatting.user.id === userId ? (
                  <SpeechBubble key={index} time={chatting.receivedTime}>
                    {chatting.content}
                  </SpeechBubble>
                ) : (
                  <SpeechBubbleWithAvatar
                    key={index}
                    time={chatting.receivedTime}
                    user={chatting.user}
                  >
                    {chatting.content}
                  </SpeechBubbleWithAvatar>
                );
              }

              return (
                <SpeechBubble key={index} type={chatting.type}>
                  {chatting.content}
                </SpeechBubble>
              );
            })}
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
  game: PropTypes.shape({
    id: PropTypes.number,
    name: PropTypes.string,
  }),
  roomId: PropTypes.number,
  match: PropTypes.object,
};

export default ChattingRoom;
