import './TableContent.scss';

import { Body2, Caption2 } from '../../core/Typography';

import { AiTwotonePushpin } from '@react-icons/all-files/ai/AiTwotonePushpin';
import { BiTimeFive } from '@react-icons/all-files/bi/BiTimeFive';
import { BsPersonFill } from '@react-icons/all-files/bs/BsPersonFill';
import { FaHotjar } from '@react-icons/all-files/fa/FaHotjar';
import InfoWithIcon from '../../components/InfoWithIcon/InfoWithIcon';
import { LEAST_LIKE_TO_HOT_POST } from '../../constants/board';
import LikeAndView from '../../chunks/LikeAndView/LikeAndView';
import PropTypes from 'prop-types';
import React from 'react';

const TableContent = ({
  boardDetails,
  onContentClick = () => {},
  onCategoryClick = () => {},
}) => {
  const {
    title,
    category,
    view,
    like,
    nickname,
    createdDate,
    createdTime,
    notice,
  } = boardDetails;

  const handleCategoryClick = (e) => {
    e.stopPropagation();

    onCategoryClick();
  };

  return (
    <main className='table-content-container' onClick={onContentClick}>
      <div className='table-content-details'>
        {notice && (
          <InfoWithIcon
            icon={<AiTwotonePushpin size='22px' />}
            color='babble-pink'
          />
        )}
        {like > LEAST_LIKE_TO_HOT_POST && !notice && (
          <InfoWithIcon icon={<FaHotjar size='18px' />} color='red' />
        )}

        <div className='table-content-wrapper'>
          <Body2>{title}</Body2>

          <span className='table-infos'>
            <button onClick={handleCategoryClick} className='category-search'>
              <Caption2>{category}</Caption2>
            </button>
            <LikeAndView view={view} like={like} />
            <InfoWithIcon
              icon={<BsPersonFill size='15px' />}
              content={nickname}
            />
          </span>
        </div>
      </div>
      <InfoWithIcon
        icon={<BiTimeFive size='15px' />}
        content={
          new Date(createdDate).getDate() >= new Date().getDate()
            ? createdTime
            : createdDate
        }
      />
    </main>
  );
};

TableContent.propTypes = {
  boardDetails: PropTypes.shape({
    title: PropTypes.string,
    category: PropTypes.string,
    nickname: PropTypes.string,
    createdAt: PropTypes.string,
    createdDate: PropTypes.string,
    createdTime: PropTypes.string,
    view: PropTypes.number,
    like: PropTypes.number,
    notice: PropTypes.bool,
  }),
  onContentClick: PropTypes.func,
  onCategoryClick: PropTypes.func,
};

export default TableContent;
