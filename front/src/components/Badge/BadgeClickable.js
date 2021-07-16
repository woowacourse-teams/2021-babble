import './Badge.scss';

import Badge from './Badge';
import PropTypes from 'prop-types';
import React from 'react';

const BadgeClickable = ({ onClick, areaLabel, children }) => {
  return (
    <button onClick={onClick} aria-label={areaLabel}>
      <Badge>{children}</Badge>
    </button>
  );
};

BadgeClickable.propTypes = {
  onClick: PropTypes.bool,
  areaLabel: PropTypes.string,
  children: PropTypes.node,
};

export default BadgeClickable;
