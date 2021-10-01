import ImageRegister from './ImageRegister';
import React from 'react';

export default {
  title: 'components/ImageRegister',
  component: [ImageRegister],
};

const ImagePreviewTemplate = (args) => (
  <div style={{ margin: '1rem' }}>
    <ImageRegister {...args} />
  </div>
);

export const General = ImagePreviewTemplate.bind({});
