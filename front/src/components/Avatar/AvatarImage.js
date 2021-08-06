import './Avatar.scss';

import PropTypes from 'prop-types';
import React from 'react';

const AvatarImage = ({
  imageSrc = 'https://i.pinimg.com/474x/1c/4b/f0/1c4bf0cdcc3102126b7caeb8749f5c55.jpg',
  size,
}) => {
  return (
    <img src={imageSrc} alt='Avatar Image' className={`avatar-image ${size}`} />
  );
};

AvatarImage.propTypes = {
  size: PropTypes.string,
  imageSrc: PropTypes.string,
};

export default AvatarImage;
