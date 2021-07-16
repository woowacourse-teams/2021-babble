import './Typography.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Caption1 = ({ children }) => {
  return <span className='caption1'>{children}</span>;
};

Caption1.propTypes = {
  children: PropTypes.node,
};

export default Caption1;
