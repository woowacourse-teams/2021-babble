import './Badge.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Badge = ({ colored = false, children }) => {
  return (
    <span className={`badge-container ${colored ? 'colored' : ''}`}>
      {children}
    </span>
  );
};

Badge.propTypes = {
  colored: PropTypes.bool,
  children: PropTypes.node,
};

export default Badge;
