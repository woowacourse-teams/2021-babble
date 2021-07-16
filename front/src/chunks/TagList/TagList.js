import './TagList.scss';

import React, { useRef } from 'react';

import Caption1 from '../../core/Typography/Caption1';
import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import Tag from '../../components/Tag/Tag';

const TagList = ({ tags }) => {
  const tagListRef = useRef();

  const onWheel = (e) => {
    if (e.deltaY > 0) {
      tagListRef.current.scrollLeft += 20;
    } else {
      tagListRef.current.scrollLeft -= 20;
    }
  };

  return (
    <div className='tag-list-container' onWheel={onWheel} ref={tagListRef}>
      <LinearLayout direction='row'>
        {tags.map((tag, index) => (
          <Tag key={index}>
            <Caption1>{tag}</Caption1>
          </Tag>
        ))}
      </LinearLayout>
    </div>
  );
};

TagList.propTypes = {
  tags: PropTypes.arrayOf(PropTypes.string),
};

export default TagList;
