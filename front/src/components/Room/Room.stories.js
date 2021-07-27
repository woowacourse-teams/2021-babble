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
  roomId: 1,
  imageSrc:
    'https://i.pinimg.com/474x/1c/4b/f0/1c4bf0cdcc3102126b7caeb8749f5c55.jpg',
  nickname: 'nickname',
  direction: 'col',
  headCount: 3,
  totalHeadCount: 10,
  tags: ['음성 가능', '브론즈'],
};
