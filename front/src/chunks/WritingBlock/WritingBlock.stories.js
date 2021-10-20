import React from 'react';
import WritingBlock from './WritingBlock';

export default {
  title: 'chunks/WritingBlock',
  component: WritingBlock,
};

const WritingBlockTemplate = (args) => <WritingBlock {...args} />;

export const Default = WritingBlockTemplate.bind({});

Default.args = {};
