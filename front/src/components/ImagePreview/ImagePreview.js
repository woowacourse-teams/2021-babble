import './ImagePreview.scss';

import PropTypes from 'prop-types';
import React from 'react';
import { Subtitle3 } from '../../core/Typography';

const ImagePreview = ({ imageList }) => {
  const [bigImage, middleImage, smallImage] = imageList;

  return (
    <section className='image-preview-section'>
      <Subtitle3>이미지 Preview</Subtitle3>
      <picture>
        <div className='preview'>
          <img src={bigImage} alt='' />
        </div>
        <div className='preview'>
          <img src={middleImage} alt='' />
        </div>
        <div className='preview'>
          <img src={smallImage} alt='' />
        </div>
      </picture>
    </section>
  );
};

export default ImagePreview;

ImagePreview.propTypes = {
  imageList: PropTypes.arrayOf(PropTypes.string).isRequired,
};
