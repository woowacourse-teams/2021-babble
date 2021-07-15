import './Button.scss';

import PropTypes from 'prop-types';
import React from 'react';

const SquareButton = ({
  size = 'medium',
  type = 'button',
  colored = true,
  children,
}) => {
  return (
    <button
      type={type}
      className={`square-button ${size} ${colored ? 'colored' : 'line'}`}
    >
      {children}
    </button>
  );
};

SquareButton.propTypes = {
  size: PropTypes.string,
  type: PropTypes.string,
  colored: PropTypes.bool,
  children: PropTypes.node,
};

export default SquareButton;
