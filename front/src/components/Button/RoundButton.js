import './Button.scss';

import PropTypes from 'prop-types';
import React from 'react';

const RoundButton = ({
  size = 'medium',
  type = 'button',
  colored = false,
  onClick,
  children,
}) => {
  return (
    <button
      type={type}
      className={`round-button ${size} ${colored ? 'colored' : 'line'}`}
      onClick={onClick}
    >
      {children}
    </button>
  );
};

RoundButton.propTypes = {
  size: PropTypes.oneOf(['tiny', 'small', 'medium', 'large', 'block']),
  type: PropTypes.oneOf(['button', 'submit']),
  colored: PropTypes.bool,
  onClick: PropTypes.func,
  children: PropTypes.node,
};

export default RoundButton;
