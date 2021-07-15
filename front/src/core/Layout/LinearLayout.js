import './LinearLayout.scss';

import PropTypes from 'prop-types';
import React from 'react';

const LinearLayout = ({ direction = 'col', children }) => {
  return <section className={`${direction} linear-layout`}>{children}</section>;
};

LinearLayout.propTypes = {
  direction: PropTypes.oneOf(['col', 'row']),
  children: PropTypes.node,
};

export default LinearLayout;
