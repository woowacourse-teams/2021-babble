import './Tag.scss';

import BadgeClickable from '../Badge/BadgeClickable';
import { IoCloseOutline } from 'react-icons/io5';
import PropTypes from 'prop-types';
import React from 'react';
import Tag from './Tag';

const TagErasable = ({ children }) => {
  return (
    <Tag customClass='erasable'>
      {children}

      <BadgeClickable>
        <IoCloseOutline size='18px' />
      </BadgeClickable>
    </Tag>
  );
};

TagErasable.propTypes = {
  children: PropTypes.node,
};

export default TagErasable;
