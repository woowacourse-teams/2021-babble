import './Typography.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Subtitle1 = ({ children }) => {
  return <h3 className='headline3'>{children}</h3>;
};

Subtitle1.propTypes = {
  children: PropTypes.node,
};

export default Subtitle1;
