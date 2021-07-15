import React from 'react';
import TagList from './TagList';

export default {
  title: 'chunks/TagList',
  component: TagList,
};

const TagListTemplate = (args) => (
  <div>
    <TagList {...args}></TagList>
  </div>
);

export const Default = TagListTemplate.bind({});

Default.args = {
  tags: ['실버', '골드 승급전', '솔로랭크'],
};
