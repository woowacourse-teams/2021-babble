import AngledButton from './AngledButton';
import React from 'react';
import RoundButton from './RoundButton';

export default {
  title: 'components/Button',
  component: [AngledButton, RoundButton],
  argTypes: {
    size: {
      options: ['small', 'medium', 'large', 'full'],
      control: { type: 'select' },
    },
  },
};

const AngledButtonTemplate = (args) => <AngledButton {...args} />;
const RoundButtonTemplate = (args) => <RoundButton {...args} />;

export const Angled = AngledButtonTemplate.bind({});

Angled.args = {
  size: 'full',
  colored: true,
  children: '방 생성하기',
};

export const Round = RoundButtonTemplate.bind({});

Round.args = {
  size: 'full',
  children: '생성하기',
};
