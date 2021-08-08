import NicknameSection from './NicknameSection';
import React from 'react';

export default {
  title: 'components/NicknameSection',
  component: NicknameSection,
};

const NicknameSectionTemplate = () => (
  <div style={{ marginTop: '10rem' }}>
    <NicknameSection />
  </div>
);

export const Default = NicknameSectionTemplate.bind({});
