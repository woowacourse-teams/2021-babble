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
      id: 3,
      nickname: 'jason',
      avatar: 'https://ca.slack-edge.com/TFELTJB7V-UJ71JS7K7-3d5b71944c85-512',
    },
    guests: [
      {
        id: 2,
        nickname: 'hyeon9mak',
        avatar: 'https://avatars.githubusercontent.com/u/37354145?v=4',
      },
      {
        id: 1,
        nickname: 'wilder',
        avatar: 'https://avatars.githubusercontent.com/u/49058669?v=4',
      },
      {
        id: 4,
        nickname: 'root',
        avatar: 'https://avatars.githubusercontent.com/u/4648244?v=4',
      },
      {
        id: 5,
        nickname: 'grooming',
        avatar: 'https://avatars.githubusercontent.com/u/26598561?v=4',
      },
      {
        id: 6,
        nickname: 'fortune',
        avatar: 'https://avatars.githubusercontent.com/u/43930419?v=4',
      },
      {
        id: 7,
        nickname: 'peter',
        avatar: 'https://avatars.githubusercontent.com/u/42052110?v=4',
      },
    ],
  },
};
