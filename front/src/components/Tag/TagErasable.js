import './Tag.scss';

import BadgeClickable from '../Badge/BadgeClickable';
import { IoCloseOutline } from '@react-icons/all-files/io5/IoCloseOutline';
import PropTypes from 'prop-types';
import React from 'react';
import Tag from './Tag';

const TagErasable = ({ onDeleteTag, children, onClickTagName }) => {
  return (
    <Tag customClass='erasable'>
      <span onClick={onClickTagName}>{children}</span>
      <BadgeClickable onClickBadge={onDeleteTag} ariaLabel='delete'>
        <IoCloseOutline size='18px' />
      </BadgeClickable>
    </Tag>
  );
};

TagErasable.propTypes = {
  children: PropTypes.node,
  onDeleteTag: PropTypes.func,
  onClickTagName: PropTypes.func,
};

export default TagErasable;
