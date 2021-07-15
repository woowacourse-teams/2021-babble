import './Avatar.scss';

import AvatarImage from './AvatarImage';
import PropTypes from 'prop-types';
import React from 'react';

const Avatar = ({ size = 'small', direction = 'col', imageSrc, children }) => {
  return (
    <figure className={direction}>
      <AvatarImage imageSrc={imageSrc} size={size} />
      <figcaption className={size}>{children}</figcaption>
    </figure>
  );
};

Avatar.propTypes = {
  size: PropTypes.string,
  direction: PropTypes.string,
  imageSrc: PropTypes.string,
  children: PropTypes.node,
};

export default Avatar;
