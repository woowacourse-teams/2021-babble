import Avatar from './Avatar';
import Body2 from '../../core/Typography/Body2';
import Caption1 from '../../core/Typography/Caption1';
import Caption2 from '../../core/Typography/Caption2';
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

const AvatarTemplate = (args) => (
  <>
    <div style={{ margin: '1rem' }}>
      <Avatar size='small' {...args}>
        <Caption2>defaultUser</Caption2>
      </Avatar>
    </div>
    <div style={{ margin: '1rem' }}>
      <Avatar size='medium' {...args}>
        <Caption1>defaultUser</Caption1>
      </Avatar>
    </div>
    <div style={{ margin: '1rem' }}>
      <Avatar size='large' {...args}>
        <Body2>defaultUser</Body2>
      </Avatar>
    </div>
  </>
);

export const Column = AvatarTemplate.bind({});

Column.args = {
  imageSrc:
    'https://i.pinimg.com/474x/1c/4b/f0/1c4bf0cdcc3102126b7caeb8749f5c55.jpg',
  direction: 'col',
};

export const Row = AvatarTemplate.bind({});

Row.args = {
  imageSrc:
    'https://i.pinimg.com/474x/1c/4b/f0/1c4bf0cdcc3102126b7caeb8749f5c55.jpg',
  direction: 'row',
};
