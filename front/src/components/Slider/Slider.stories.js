import React from 'react';
import Slider from './Slider';

export default {
  title: 'components/Slider',
  component: Slider,
};

const SliderTemplate = (args) => (
  <>
    <Slider {...args} />
  </>
);

export const DefaultSlider = SliderTemplate.bind({});

DefaultSlider.args = {
  imageList: [
    {
      id: 0,
      info: '곰돌이 푸',
      src: 'https://picsum.photos/id/10/1280/700',
    },
    {
      id: 1,
      info: '피글렛',
      src: 'https://picsum.photos/id/20/1280/700',
    },
    {
      id: 2,
      info: '티거',
      src: 'https://picsum.photos/id/30/1280/700',
    },
    {
      id: 3,
      info: '이요르',
      src: 'https://picsum.photos/id/40/1280/700',
    },
    {
      id: 4,
      info: '이요르',
      src: 'https://picsum.photos/id/50/1280/700',
    },
  ],
};
