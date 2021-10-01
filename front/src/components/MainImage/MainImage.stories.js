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

Default.args = {
  imageSrc:
    'https://d2bidcnq0n74fu.cloudfront.net/img/games/title/League-of-Legends.png',
};
