import './ImagePreview.scss';

import PropTypes from 'prop-types';
import React from 'react';
import { Subtitle3 } from '../../core/Typography';

const ImagePreview = ({ imageList }) => {
  const [bigImage, middleImage, smallImage] = imageList;

  return (
    <section className='image-preview-container'>
      <Subtitle3>이미지 Preview</Subtitle3>
      <div className='image-preview-wrapper'>
        <div className='preview'>
          <img src={bigImage.imagePath} alt='Big image preview' />
        </div>
        <div className='preview'>
          <img src={middleImage.imagePath} alt='Middle image preview' />
        </div>
        <div className='preview'>
          <img src={smallImage.imagePath} alt='Small image preview' />
        </div>
      </div>
    </section>
  );
};

export default ImagePreview;

ImagePreview.propTypes = {
  imageList: PropTypes.arrayOf(
    PropTypes.shape({ name: PropTypes.string, imagePath: PropTypes.string })
  ).isRequired,
};
