import './Typography.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Subtitle2 = ({ children }) => {
  return <h4 className='subtitle2'>{children}</h4>;
};

Subtitle2.propTypes = {
  children: PropTypes.node,
};

export default Subtitle2;
