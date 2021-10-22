import MainImage from './MainImage';
import React from 'react';

export default {
  title: 'components/MainImage',
  component: MainImage,
};

const MainImageTemplate = (args) => (
  <div>
    <MainImage {...args} />
  </div>
);

export const Default = MainImageTemplate.bind({});

Default.args = {
  imageSrc: 'https://babble.gg/img/games/title/League%20of%20Legends-x1920.jpg',
};
