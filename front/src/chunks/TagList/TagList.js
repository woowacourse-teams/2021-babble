import './TagList.scss';

import { Tag, TagErasable } from '../../components';

import { Caption1 } from '../../core/Typography';
import LinearLayout from '../../core/Layout/LinearLayout';
import PropTypes from 'prop-types';
import React from 'react';

const TagList = ({ tags, onDeleteTag, erasable = false }) => {
  return (
    <div className='tag-list-container'>
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
