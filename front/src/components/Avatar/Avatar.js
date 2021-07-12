import './Avatar.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Avatar = ({
  size = 'small',
  nickName,
  direction = 'vertical',
  imageSrc,
}) => {
  return (
    <figure className={direction}>
      <img src={imageSrc} alt='Avatar Image' className={size} />
      <figcaption className={size}>{nickName}</figcaption>
    </figure>
  );
};

Avatar.propTypes = {
  size: PropTypes.string,
  nickName: PropTypes.string,
  direction: PropTypes.string,
  imageSrc: PropTypes.string,
};

export default Avatar;
