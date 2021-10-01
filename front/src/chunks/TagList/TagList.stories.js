import React from 'react';
import TagList from './TagList';

export default {
  title: 'chunks/TagList',
  component: TagList,
};

const TagListTemplate = (args) => <TagList {...args}></TagList>;

export const Default = TagListTemplate.bind({});

Default.args = {
  tags: [
    { name: '실버' },
    { name: '골드 승급전' },
    { name: '원딜' },
    { name: '솔로랭크' },
    { name: '음성채팅 가능' },
    { name: '버스태워줄 사람' },
    { name: '문신있음' },
    { name: '85kg' },
    { name: '190cm' },
    { name: '노스페이스 패딩' },
    { name: '뉴발란스 신발' },
    { name: '캘빈클라인 벨트' },
    { name: '자가 집 보유' },
    { name: '자동차 보유' },
  ],
  erasable: false,
};
