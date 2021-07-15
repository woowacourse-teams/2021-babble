import ChattingArea from './ChattingArea';
import React from 'react';

export default {
  title: 'components/ChattingArea',
  component: ChattingArea,
};

const ChattingAreaTemplate = (args) => <ChattingArea {...args} />;

export const Default = ChattingAreaTemplate.bind({});

Default.args = {};
