import LikeAndView from './LikeAndView';
import React from 'react';

export default {
  title: 'chunks/LikeAndView',
  component: LikeAndView,
};

const LikeAndViewTemplate = (args) => (
  <div>
    <LikeAndView {...args} />
  </div>
);

export const Default = LikeAndViewTemplate.bind({});

Default.args = {
  like: '12938',
  view: '18792',
};
