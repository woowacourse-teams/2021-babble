import React from 'react';
import SpeechBubble from './SpeechBubble';

export default {
  title: 'components/SpeechBubble',
  component: SpeechBubble,
  argTypes: {
    type: {
      options: ['others', 'mine'],
      control: { type: 'select' },
    },
  },
};

const SpeechBubbleTemplate = (args) => <SpeechBubble {...args} />;

export const DefaultTemplate = SpeechBubbleTemplate.bind({});

DefaultTemplate.args = {
  time: '10:51AM',
  type: 'others',
  children: '안녕하세요~~ \n 같이 롤하실?~~~ ',
};
