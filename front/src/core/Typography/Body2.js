import './Typography.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Body2 = ({ children }) => {
  return <p className='body2'>{children}</p>;
};

Body2.propTypes = {
  children: PropTypes.node,
};

export default Body2;
