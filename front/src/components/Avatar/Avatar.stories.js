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
    'https://mblogthumb-phinf.pstatic.net/20150427_261/ninevincent_1430122791768m7oO1_JPEG/kakao_1.jpg?type=w2',
  nickName: 'defaultUser 매맘밍뎅',
  direction: 'vertical',
};

export const Horizontal = AvatarTemplate.bind({});

Horizontal.args = {
  size: 'medium',
  imageSrc:
    'https://mblogthumb-phinf.pstatic.net/20150427_261/ninevincent_1430122791768m7oO1_JPEG/kakao_1.jpg?type=w2',
  nickName: 'defaultUser',
  direction: 'horizontal',
};
