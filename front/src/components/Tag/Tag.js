import './Tag.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Tag = ({ customClass = '', children, onClickTagName = null }) => {
  return (
    <span className={`tag-container ${customClass}`}>
      <span className='tag-content' onClick={onClickTagName}>
        {children}
      </span>
    </span>
  );
};

Tag.propTypes = {
  children: PropTypes.node,
  customClass: PropTypes.string,
};

export default Tag;
