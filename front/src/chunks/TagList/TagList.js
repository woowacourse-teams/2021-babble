import './TagList.scss';

import React, { useRef } from 'react';

import Caption1 from '../../core/Typography/Caption1';
import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import Tag from '../../components/Tag/Tag';
import TagErasable from '../../components/Tag/TagErasable';

const TagList = ({ tags, erasable = false, useWheel = false }) => {
  const tagListRef = useRef();

  const onWheel = (e) => {
    if (e.deltaY > 0) {
      tagListRef.current.scrollLeft += 20;
    } else {
      tagListRef.current.scrollLeft -= 20;
    }
  };

  return (
    <div
      className={`${useWheel ? '' : 'default'} tag-list-container`}
      onWheel={useWheel ? onWheel : null}
      ref={tagListRef}
    >
      <LinearLayout direction='row'>
        {erasable
          ? tags.map((tag, index) => (
              <TagErasable key={index}>
                <Caption1>{tag.name}</Caption1>
              </TagErasable>
            ))
          : tags.map((tag, index) => (
              <Tag key={index}>
                <Caption1>{tag.name}</Caption1>
              </Tag>
            ))}
      </LinearLayout>
    </div>
  );
};

TagList.propTypes = {
  tags: PropTypes.arrayOf(PropTypes.shape({ name: PropTypes.string })),
  erasable: PropTypes.bool,
  useWheel: PropTypes.bool,
};

export default TagList;
