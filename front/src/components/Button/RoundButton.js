import './Button.scss';

import PropTypes from 'prop-types';
import React from 'react';

const RoundButton = ({
  size = 'medium',
  type = 'button',
  colored = false,
  children,
}) => {
  return (
    <button
      type={type}
      className={`round-button ${size} ${colored ? 'colored' : 'line'}`}
    >
      {children}
    </button>
  );
};

RoundButton.propTypes = {
  size: PropTypes.string,
  type: PropTypes.string,
  colored: PropTypes.bool,
  children: PropTypes.node,
};

export default RoundButton;
