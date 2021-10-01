import ImagePreview from './ImagePreview';
import React from 'react';

export default {
  title: 'components/ImagePreview',
  component: [ImagePreview],
};

const imageList = {
  bigImage:
    'https://d2bidcnq0n74fu.cloudfront.net/img/games/title/League-of-Legends.png',
  middleImage:
    'https://d2bidcnq0n74fu.cloudfront.net/img/games/title/League-of-Legends.png',
  smallImage:
    'https://d2bidcnq0n74fu.cloudfront.net/img/games/title/League-of-Legends.png',
};

const ImagePreviewTemplate = (args) => (
  <div style={{ margin: '1rem' }}>
    <ImagePreview imageList={imageList} {...args} />
  </div>
);

export const General = ImagePreviewTemplate.bind({});
