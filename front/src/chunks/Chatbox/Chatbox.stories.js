import Chatbox from './Chatbox';
import React from 'react';

export default {
  title: 'chunks/Chatbox',
  component: Chatbox,
};

const ChatboxTemplate = (args) => <Chatbox {...args} />;

export const Default = ChatboxTemplate.bind({});

Default.args = {
  roomNo: 10,
  createdAt: new Date(),
};
