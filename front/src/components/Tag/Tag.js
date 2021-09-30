import './Tag.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Tag = ({ customClass = '', children, ...rest }) => {
  return (
    <span className={`tag-container ${customClass}`}>
      {customClass.includes('erasable') ? (
        children
      ) : (
        <span {...rest}>{children}</span>
      )}
    </span>
  );
};

Tag.propTypes = {
  children: PropTypes.node,
  customClass: PropTypes.string,
};

export default Tag;
