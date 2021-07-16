import Chatbox from './Chatbox';
import React from 'react';
import SpeechBubble from '../../components/SpeechBubble/SpeechBubble';
import SpeechBubbleWithAvatar from '../../components/SpeechBubble/SpeechBubbleWithAvatar';
import { withA11y } from '@storybook/addon-a11y';

export default {
  title: 'chunks/Chatbox',
  decorators: [withA11y],
  component: Chatbox,
};

const ChatboxTemplate = (args) => (
  <div>
    <Chatbox {...args}>
      <SpeechBubbleWithAvatar time='10:19AM' nickname='peter'>
        안녕하세요
      </SpeechBubbleWithAvatar>
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-end',
        }}
      >
        <SpeechBubble type='mine' time='10:21AM'>
          오
        </SpeechBubble>
        <SpeechBubble type='mine' time='10:21AM'>
          안녕하세요
        </SpeechBubble>
      </div>
    </Chatbox>
  </div>
);

export const Default = ChatboxTemplate.bind({});

Default.args = {
  roomNo: 10,
  createdAt: '2021-07-05T00:00:00.000Z',
};
