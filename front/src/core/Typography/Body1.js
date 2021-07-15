import './Typography.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Body1 = ({ children }) => {
  return <p className='body1'>{children}</p>;
};

Body1.propTypes = {
  children: PropTypes.node,
};

export default Body1;
