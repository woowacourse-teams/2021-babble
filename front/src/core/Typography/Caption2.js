import './Typography.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Caption2 = ({ customClass, children, ...rest }) => {
  return (
    <span className={`caption2 ${customClass}`} {...rest}>
      {children}
    </span>
  );
};

Caption2.propTypes = {
  customClass: PropTypes.string,
  children: PropTypes.node,
};

export default Caption2;
