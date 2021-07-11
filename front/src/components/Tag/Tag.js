import './Tag.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Tag = ({ customClass, children }) => {
  return <span className={`tag-container ${customClass}`}>{children}</span>;
};

Tag.propTypes = {
  children: PropTypes.node,
  customClass: PropTypes.string,
};

export default Tag;
