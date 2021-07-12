import './Button.scss';

import PropTypes from 'prop-types';
import React from 'react';

const AngledButton = ({
  size = 'full',
  type = 'button',
  colored = true,
  children,
}) => {
  return (
    <button
      type={type}
      className={`angled-button ${size} ${colored ? 'colored' : 'plain'}`}
    >
      {children}
    </button>
  );
};

AngledButton.propTypes = {
  size: PropTypes.string,
  type: PropTypes.string,
  colored: PropTypes.bool,
  children: PropTypes.node,
};

export default AngledButton;
