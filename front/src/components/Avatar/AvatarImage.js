import './Avatar.scss';

import PropTypes from 'prop-types';
import React from 'react';

const AvatarImage = ({ imageSrc, size }) => {
  return (
    <img src={imageSrc} alt='Avatar Image' className={`avatar-image ${size}`} />
  );
};

AvatarImage.propTypes = {
  size: PropTypes.string,
  imageSrc: PropTypes.string,
};

export default AvatarImage;
