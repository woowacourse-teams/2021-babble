import './Badge.scss';

import Badge from './Badge';
import PropTypes from 'prop-types';
import React from 'react';

const BadgeClickable = ({ onClickBadge, ariaLabel, children }) => {
  return (
    <button onClick={onClickBadge} aria-label={ariaLabel}>
      <Badge>{children}</Badge>
    </button>
  );
};

BadgeClickable.propTypes = {
  onClickBadge: PropTypes.func,
  ariaLabel: PropTypes.string,
  children: PropTypes.node,
};

export default BadgeClickable;
