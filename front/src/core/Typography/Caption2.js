import './Typography.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Caption2 = ({ children }) => {
  return <span className='caption2'>{children}</span>;
};

Caption2.propTypes = {
  children: PropTypes.node,
};

export default Caption2;
