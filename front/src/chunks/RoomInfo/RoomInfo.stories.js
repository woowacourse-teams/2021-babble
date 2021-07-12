import React from 'react';
import RoomInfo from './RoomInfo';

export default {
  title: 'chunks/RoomInfo',
  component: RoomInfo,
};

const RoomInfoTemplate = (args) => <RoomInfo {...args} />;

export const Default = RoomInfoTemplate.bind({});

Default.args = {
  gameTitle: 'League of Legends',
  host: { avatar: '', nickName: 'wilder' },
  guests: [
    { avatar: '', nickName: 'Hyeon9mak' },
    { avatar: '', nickName: 'root' },
    { avatar: '', nickName: 'fortune' },
  ],
  tags: [
    '실버',
    '2시간',
    '솔로랭크',
    '음성채팅 가능',
    '3대 500',
    '문신 있음',
    '흡연자',
    '75kg',
  ],
};
