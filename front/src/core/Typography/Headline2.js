import './Typography.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Headline2 = ({ bold = false, children }) => {
  return <h2 className={`headline2 ${bold ? 'bold' : ''}`}>{children}</h2>;
};

Headline2.propTypes = {
  bold: PropTypes.string,
  children: PropTypes.node,
};

export default Headline2;
