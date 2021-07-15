import React from 'react';
import SpeechBubble from './SpeechBubble';
import SpeechBubbleWithAvatar from './SpeechBubbleWithAvatar';

export default {
  title: 'components/SpeechBubble',
  component: [SpeechBubble, SpeechBubbleWithAvatar],
};

const SpeechBubbleTemplate = (args) => (
  <div
    style={{
      'height': '20rem',
      'display': 'flex',
      'flex-direction': 'column',
      'justify-content': 'space-around',
    }}
  >
    <SpeechBubble {...args} />
    <SpeechBubble type='mine' {...args} />
  </div>
);

export const Default = SpeechBubbleTemplate.bind({});

Default.args = {
  time: '10:51AM',
  children: '안녕하세요~~ \n같이 롤하실?~~~ ',
};

const SpeechBubbleWithAvatarTemplate = (args) => (
  <SpeechBubbleWithAvatar {...args} />
);

export const WithAvatar = SpeechBubbleWithAvatarTemplate.bind({});

WithAvatar.args = {
  size: 'small',
  nickname: 'hyun9mac',
  time: '10:51AM',
  children: '안녕하세요~~ \n같이 롤하실?~~~ ',
};
