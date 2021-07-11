import React from 'react';
import Tag from './Tag';
import TagErasable from './TagErasable';

export default {
  title: 'components/Tag',
  component: [Tag, TagErasable],
};

const TagTemplate = (args) => <Tag {...args} />;
const TagErasableTemplate = (args) => <TagErasable {...args} />;

export const General = TagTemplate.bind({});

General.args = {
  children: '실버',
};

export const Erasable = TagErasableTemplate.bind({});

Erasable.args = {
  children: '실버',
};
