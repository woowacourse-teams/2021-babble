import React from 'react';
import SpeechBubble from './SpeechBubble';
import SpeechBubbleWithAvatar from './SpeechBubbleWithAvatar';
import Subtitle3 from '../../core/Typography/Subtitle3';

export default {
  title: 'components/SpeechBubble',
  component: [SpeechBubble, SpeechBubbleWithAvatar],
};

const SpeechBubbleTemplate = (args) => (
  <div
    style={{
      height: '20rem',
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'space-around',
    }}
  >
    <div style={{ margin: '8rem 0 1rem 0' }}>
      <Subtitle3>상대방의 채팅</Subtitle3>
      <br />
      <SpeechBubble {...args} />
    </div>
    <div style={{ marginBottom: '1rem' }}>
      <Subtitle3>나의 채팅</Subtitle3>
      <br />
      <SpeechBubble type='mine' {...args} />
    </div>
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
