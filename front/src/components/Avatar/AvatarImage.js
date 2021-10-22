import './Avatar.scss';

import PropTypes from 'prop-types';
import React from 'react';

const AvatarImage = ({
  imageSrc = 'https://i.pinimg.com/474x/1c/4b/f0/1c4bf0cdcc3102126b7caeb8749f5c55.jpg',
  size,
  visibility,
}) => {
  return (
    <div className={`avatar-image-container ${size} ${visibility}`}>
      <img src={imageSrc} alt='Avatar Image' className='avatar-image' />
    </div>
  );
};

AvatarImage.propTypes = {
  size: PropTypes.string,
  imageSrc: PropTypes.string,
  visibility: PropTypes.string,
};

export default AvatarImage;
