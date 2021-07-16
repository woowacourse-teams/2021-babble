import ChattingForm from './ChattingForm';
import React from 'react';

export default {
  title: 'components/ChattingForm',
  component: ChattingForm,
};

const ChattingFormTemplate = (args) => <ChattingForm {...args} />;

export const Default = ChattingFormTemplate.bind({});

Default.args = {};
