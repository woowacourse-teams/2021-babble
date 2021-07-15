import './TagList.scss';

import Caption1 from '../../core/Typography/Caption1';
import { IoCloseOutline } from 'react-icons/io5';
import { IoRemove } from 'react-icons/io5';
import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import React from 'react';
import Subtitle3 from '../../core/Typography/Subtitle3';
import Tag from '../../components/Tag/Tag';

const TagList = ({ tags }) => {
  return (
    <div className='tag-list-container'>
      <LinearLayout direction='row'>
        <LinearLayout direction='row'>
          <Subtitle3>태그</Subtitle3>
          {tags.map((tag, idx) => (
            <Tag key={idx}>
              <Caption1>{tag}</Caption1>
            </Tag>
          ))}
        </LinearLayout>
        <LinearLayout direction='row'>
          <button className='room-minimize'>
            <IoRemove size='24px' />
          </button>
          <button className='room-exit'>
            <IoCloseOutline size='24px' />
          </button>
        </LinearLayout>
      </LinearLayout>
    </div>
  );
};

TagList.propTypes = {
  tags: PropTypes.arrayOf(PropTypes.string),
};

export default TagList;
