import './InfoWithIcon.scss';

import { Caption2 } from '../../core/Typography';
import PropTypes from 'prop-types';
import React from 'react';

const InfoWithIcon = ({ icon, content = '', color = 'grey' }) => {
  return (
    <span className={`info-with-icon-container ${color}`}>
      {icon}
      <Caption2>{content}</Caption2>
    </span>
  );
};

InfoWithIcon.propTypes = {
  icon: PropTypes.node,
  color: PropTypes.string,
  content: PropTypes.string,
};
export default InfoWithIcon;
