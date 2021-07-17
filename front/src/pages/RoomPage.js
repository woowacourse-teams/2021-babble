import './RoomPage.scss';

import Avatar from '../components/Avatar/Avatar';
import Chatbox from '../chunks/Chatbox/Chatbox';
import React from 'react';
import SpeechBubble from '../components/SpeechBubble/SpeechBubble';
import { response } from '../../mockData';

const RoomPage = () => {
  const { roomId, createdDate } = response;
  return (
    <main>
      <Chatbox roomId={roomId} createdAt={createdDate}>
        <div
          style={{
            display: 'flex',
            justifyContent: 'flex-start',
            width: '100%',
            paddingLeft: '1rem',
          }}
        >
          <Avatar size='small' nickName='Hyeon9mak' />
          <SpeechBubble time='10:57' type='others'>
            하이요 겜합시다 하이요 겜합시다 하이요 겜합시다 하이요 겜합시다
            하이요 겜합시다 하이요 겜합시다 하이요 겜합시다하이요 겜합시다
            하이요 겜합시다하이요 겜합시다 하이요 겜합시다
          </SpeechBubble>
        </div>
        <div
          style={{
            display: 'flex',
            justifyContent: 'flex-end',
            width: '100%',
            paddingRight: '1rem',
          }}
        >
          <SpeechBubble time='10:58' type='mine'>
            오 안녕하세요 ㅋ
          </SpeechBubble>
        </div>
      </Chatbox>
    </main>
  );
};

export default RoomPage;
