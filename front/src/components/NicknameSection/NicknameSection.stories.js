import NicknameSection from './NicknameSection';
import React from 'react';

export default {
  title: 'components/NicknameSection',
  component: NicknameSection,
};

const NicknameSectionTemplate = (args) => <NicknameSection {...args} />;

export const Default = NicknameSectionTemplate.bind({});
Default.args = {
  nickname: 'fortune',
};
