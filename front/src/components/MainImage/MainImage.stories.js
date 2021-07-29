import MainImage from './MainImage';
import React from 'react';

export default {
  title: 'chunks/MainImage',
  component: MainImage,
};

const MainImageTemplate = (args) => (
  <div>
    <MainImage {...args} />
  </div>
);

export const Default = MainImageTemplate.bind({});

Default.args = {};
