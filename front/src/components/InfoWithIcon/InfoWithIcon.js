import './InfoWithIcon.scss';

import { Caption2 } from '../../core/Typography';
import PropTypes from 'prop-types';
import React from 'react';

const InfoWithIcon = ({ icon, content }) => {
  return (
    <span className='watch-container'>
      {icon}
      <Caption2>{content}</Caption2>
    </span>
  );
};

InfoWithIcon.propTypes = {
  icon: PropTypes.node,
  content: PropTypes.string,
};
export default InfoWithIcon;
