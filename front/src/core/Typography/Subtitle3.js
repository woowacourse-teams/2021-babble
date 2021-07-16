import './Typography.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Subtitle3 = ({ children }) => {
  return <h5>{children}</h5>;
};

Subtitle3.propTypes = {
  children: PropTypes.node,
};

export default Subtitle3;
