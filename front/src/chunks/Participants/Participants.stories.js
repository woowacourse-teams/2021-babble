import Participants from './Participants';
import React from 'react';

export default {
  title: 'chunks/Participants',
  component: Participants,
};

const ParticipantsTemplate = (args) => <Participants {...args} />;

export const Default = ParticipantsTemplate.bind({});

Default.args = {
  participants: {
    host: {
      id: 1,
      nickname: 'wilder',
    },
    guests: [
      {
        id: 2,
        nickname: 'hyeon9mak',
      },
      {
        id: 3,
        nickname: 'jason',
      },
    ],
  },
};
