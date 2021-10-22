import './LikeAndView.scss';

import { AiOutlineEye } from '@react-icons/all-files/ai/AiOutlineEye';
import { AiOutlineLike } from '@react-icons/all-files/ai/AiOutlineLike';
import InfoWithIcon from '../../components/InfoWithIcon/InfoWithIcon';
import PropTypes from 'prop-types';
import React from 'react';

const LikeAndView = ({ like, view }) => {
  return (
    <span className='like-and-view-container'>
      <InfoWithIcon icon={<AiOutlineLike size='15px' />} content={like} />
      <InfoWithIcon icon={<AiOutlineEye size='16px' />} content={view} />
    </span>
  );
};

LikeAndView.propTypes = {
  like: PropTypes.number,
  view: PropTypes.number,
};

export default LikeAndView;
