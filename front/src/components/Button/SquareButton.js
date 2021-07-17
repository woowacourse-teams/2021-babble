import './Button.scss';

import PropTypes from 'prop-types';
import React from 'react';

const SquareButton = ({
  size = 'medium',
  type = 'button',
  colored = true,
  children,
  ...rest
}) => {
  return (
    <button
      type={type}
      className={`square-button ${size} ${colored ? 'colored' : 'line'}`}
      {...rest}
    >
      {children}
    </button>
  );
};

SquareButton.propTypes = {
  size: PropTypes.string,
  type: PropTypes.string,
  colored: PropTypes.bool,
  onClick: PropTypes.func,
  children: PropTypes.node,
};

export default SquareButton;
