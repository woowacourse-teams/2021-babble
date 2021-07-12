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

export const Default = SpeechBubbleTemplate.bind({});

Default.args = {
  time: '10:51AM',
  type: 'others',
  children: '안녕하세요~~ \n같이 롤하실?~~~ ',
};
