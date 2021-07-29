import './PageLayout.scss';

import PropTypes from 'prop-types';
import React from 'react';

const PageLayout = ({ type = 'default', direction = 'column', children }) => {
  return (
    <section className={`${type} ${direction} page-layout`}>{children}</section>
  );
};

PageLayout.propTypes = {
  type: PropTypes.oneOf(['default', 'narrow']),
  direction: PropTypes.oneOf(['column', 'row']),
  children: PropTypes.node,
};

export default PageLayout;
