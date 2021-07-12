import Logo from './Logo';
import React from 'react';

export default {
  title: 'components/Logo',
  component: Logo,
};

const LogoTemplate = (args) => <Logo {...args} />;

export const Default = LogoTemplate.bind({});

Default.args = {
  width: 150,
};
