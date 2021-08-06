import './Typography.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Headline1 = ({ bold = false, children }) => {
  return <h1 className={`headline1 ${bold ? 'bold' : ''}`}>{children}</h1>;
};

Headline1.propTypes = {
  bold: PropTypes.string,
  children: PropTypes.node,
};

export default Headline1;
