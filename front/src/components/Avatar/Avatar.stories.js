import Avatar from './Avatar';
import React from 'react';

export default {
  title: 'components/Avatar',
  component: Avatar,
  argTypes: {
    size: {
      options: ['small', 'medium', 'large'],
      control: { type: 'select' },
    },
  },
};

const AvatarTemplate = (args) => <Avatar {...args} />;

export const Vertical = AvatarTemplate.bind({});

Vertical.args = {
  size: 'medium',
  imageSrc:
    'https://i.pinimg.com/474x/1c/4b/f0/1c4bf0cdcc3102126b7caeb8749f5c55.jpg',
  nickName: 'defaultUser 매맘밍뎅',
  direction: 'vertical',
};

export const Horizontal = AvatarTemplate.bind({});

Horizontal.args = {
  size: 'medium',
  imageSrc:
    'https://i.pinimg.com/474x/1c/4b/f0/1c4bf0cdcc3102126b7caeb8749f5c55.jpg',
  nickName: 'defaultUser',
  direction: 'horizontal',
};
