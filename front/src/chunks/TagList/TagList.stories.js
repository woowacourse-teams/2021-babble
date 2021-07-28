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
  tags: [
    '실버',
    '골드 승급전',
    '원딜',
    '솔로랭크',
    '음성채팅 가능',
    '버스태워줄 사람',
    '문신있음',
    '85kg',
    '190cm',
    '노스페이스 패딩',
    '뉴발란스 신발',
    '캘빈클라인 벨트',
    '자가 집 보유',
    '자동차 보유',
  ],
};
