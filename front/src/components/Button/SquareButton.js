import './Button.scss';

import PropTypes from 'prop-types';
import React from 'react';

const SquareButton = ({
  size = 'medium',
  type = 'button',
  colored = true,
  name,
  children,
  onClickButton = () => {},
  ...rest
}) => {
  return (
    <button
      type={type}
      className={`square-button ${size} ${colored ? 'colored' : 'line'}`}
      name={name}
      onClick={onClickButton}
      {...rest}
    >
      {children}
    </button>
  );
};

SquareButton.propTypes = {
  size: PropTypes.oneOf(['tiny', 'small', 'medium', 'large', 'block']),
  type: PropTypes.oneOf(['button', 'submit', 'reset']),
  name: PropTypes.string,
  colored: PropTypes.bool,
  onClickButton: PropTypes.func,
  children: PropTypes.node,
};

export default SquareButton;
