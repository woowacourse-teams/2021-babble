import './TagList.scss';

import React, { useRef } from 'react';
import { Tag, TagErasable } from '../../components';

import { Caption1 } from '../../core/Typography';
import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import { SCROLL } from '../../constants/event';

const TagList = ({ tags, onDeleteTag, erasable = false, useWheel = false }) => {
  const tagListRef = useRef();

  const onWheel = (e) => {
    if (e.deltaY > SCROLL.NEUTRAL) {
      tagListRef.current.scrollLeft += SCROLL.STEP;
    } else {
      tagListRef.current.scrollLeft -= SCROLL.STEP;
    }
  };

  return (
    <div
      className={`${useWheel ? '' : SCROLL.BLOCKED} tag-list-container`}
      onWheel={useWheel ? onWheel : null}
      ref={tagListRef}
    >
      <LinearLayout direction='row'>
        {erasable
          ? tags.map((tag, index) => (
              <TagErasable key={index} onDeleteTag={onDeleteTag}>
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
  onDeleteTag: PropTypes.func,
  erasable: PropTypes.bool,
  useWheel: PropTypes.bool,
};

export default TagList;
