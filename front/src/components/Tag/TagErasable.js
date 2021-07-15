import './Tag.scss';

import { IoCloseOutline } from 'react-icons/io5';
import PropTypes from 'prop-types';
import React from 'react';
import Tag from './Tag';

const TagErasable = ({ children }) => {
  return (
    <Tag customClass='erasable'>
      {children}
      <button className='tag-delete'>
        <IoCloseOutline size='18px' />
      </button>
    </Tag>
  );
};

TagErasable.propTypes = {
  children: PropTypes.node,
};

export default TagErasable;
