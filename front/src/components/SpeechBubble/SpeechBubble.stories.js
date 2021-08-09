import React from 'react';
import SpeechBubble from './SpeechBubble';
import SpeechBubbleWithAvatar from './SpeechBubbleWithAvatar';
import { Subtitle3 } from '../../core/Typography';

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
    <div style={{ margin: '20rem 0 1rem 0' }}>
      <Subtitle3>Others' Chattings</Subtitle3>
      <br />
      <SpeechBubbleWithAvatar {...args} />
    </div>
    <div style={{ marginBottom: '1rem' }}>
      <Subtitle3>My Chatting</Subtitle3>
      <br />
      <SpeechBubble type='mine'>
        {`아니요 ㅋㅋ;
전 오버워치 하러 갈건데;`}
      </SpeechBubble>
    </div>
    <div style={{ marginBottom: '1rem' }}>
      <Subtitle3>Notice</Subtitle3>
      <br />
      <SpeechBubble type='notice'>피터님이 입장하셨습니다.</SpeechBubble>
    </div>
  </div>
);

export const Default = SpeechBubbleTemplate.bind({});

Default.args = {
  time: '10:51AM',
  children: '안녕하세요~~ \n같이 롤하실?~~~ ',
};
