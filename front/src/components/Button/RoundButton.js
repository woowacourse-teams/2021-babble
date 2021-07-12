import './Button.scss';

import PropTypes from 'prop-types';
import React from 'react';

const RoundButton = ({ size, type = 'button', colored = false, children }) => {
  return (
    <button
      type={type}
      className={`round-button ${size} ${colored ? 'colored' : 'plain'}`}
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
