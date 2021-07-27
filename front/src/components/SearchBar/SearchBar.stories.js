import React from 'react';
import SearchBar from './SearchBar';

export default {
  title: 'components/SearchBar',
  component: SearchBar,
};

const SearchBarTemplate = (args) => <SearchBar {...args} />;

export const Default = SearchBarTemplate.bind({});
Default.args = {
  placeholder: '태그를 검색해주세요!',
  autoCompleteKeywords: [
    { name: 'League of Legends' },
    { name: 'Apex Legends' },
    { name: 'PUBG: BATTLEGROUNDS' },
    { name: 'Overwatch' },
    { name: 'Fall Guys' },
    { name: 'Among Us' },
    { name: 'Kartrider Rush Plus' },
    { name: 'Tekken 7' },
    { name: 'Hearthstone' },
    { name: 'Starcraft' },
    { name: 'VALORANT' },
    { name: 'Minecraft' },
    { name: 'Lost Ark' },
    { name: 'Dead by Daylight' },
    { name: 'Escape from Tarkov' },
    { name: 'Teamfight Tactics' },
    { name: '크레이지 아케이드' },
    { name: '2시간' },
    { name: '크로스파이어' },
  ],
};
