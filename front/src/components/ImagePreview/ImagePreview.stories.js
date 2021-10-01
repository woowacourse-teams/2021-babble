import ImagePreview from './ImagePreview';
import React from 'react';

export default {
  title: 'components/ImagePreview',
  component: [ImagePreview],
};

const imageList = [
  'https://d2bidcnq0n74fu.cloudfront.net/img/games/title/League-of-Legends.png',
  'https://d2bidcnq0n74fu.cloudfront.net/img/games/title/League-of-Legends.png',
  'https://d2bidcnq0n74fu.cloudfront.net/img/games/title/League-of-Legends.png',
];

const ImagePreviewTemplate = (args) => (
  <div style={{ margin: '1rem' }}>
    <ImagePreview imageList={imageList} {...args} />
  </div>
);

export const General = ImagePreviewTemplate.bind({});
