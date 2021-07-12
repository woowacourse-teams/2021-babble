import ChattingSection from './ChattingSection';
import React from 'react';

export default {
  title: 'components/ChattingSection',
  component: ChattingSection,
};

const ChattingSectionTemplate = (args) => <ChattingSection {...args} />;

export const Default = ChattingSectionTemplate.bind({});

Default.args = {};
