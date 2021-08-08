import React from 'react';
import Room from './Room';

export default {
  title: 'components/Room',
  component: Room,
};

const RoomTemplate = (args) => (
  <>
    <Room {...args} />
  </>
);

export const Column = RoomTemplate.bind({});

Column.args = {
  imageSrc:
    'https://i.pinimg.com/474x/1c/4b/f0/1c4bf0cdcc3102126b7caeb8749f5c55.jpg',
  onClickRoom: () => {},
  room: {
    roomId: 1,
    host: {
      id: 1,
      nickname: 'nickname',
    },
    headCount: {
      current: 2,
      max: 3,
    },
    tags: [
      { name: '골드' },
      { name: '초보' },
      { name: '2시간' },
      { name: '음성 가능' },
    ],
  },
};
