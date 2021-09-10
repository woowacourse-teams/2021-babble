import './Avatar.scss';

import PropTypes from 'prop-types';
import React from 'react';

const AvatarImage = ({ imageSrc, size }) => {
  return (
    <div className={`avatar-image-container ${size}`}>
      <img src={imageSrc} alt='Avatar Image' className='avatar-image' />
    </div>
  );
};

AvatarImage.propTypes = {
  size: PropTypes.string,
  imageSrc: PropTypes.string,
};

export default AvatarImage;
