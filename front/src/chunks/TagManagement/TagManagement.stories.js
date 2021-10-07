import React from 'react';
import TagManagement from './TagManagement';

export default {
  title: 'components/TagManagement',
  component: TagManagement,
};

const TagManagementTemplate = (args) => <TagManagement {...args} />;

export const Default = TagManagementTemplate.bind({});

Default.args = {
  admins: [
    { id: 0, name: '1 시간', alternativeNames: ['한 시간', '1 hour'] },
    { id: 1, name: '2 시간', alternativeNames: ['두 시간', '2 hour'] },
    { id: 2, name: '솔로 랭크', alternativeNames: ['솔랭'] },
    { id: 3, name: '듀오 랭크', alternativeNames: ['듀오랭'] },
  ],
};
