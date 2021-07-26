import DropdownInput from './DropdownInput';
import React from 'react';
import SearchInput from './SearchInput';

export default {
  title: 'components/SearchInput',
  component: [SearchInput, DropdownInput],
};

const DefaultTemplate = ({
  placeholder,
  autoCompleteKeywords,
  dropdownKeywords,
}) => (
  <div>
    <SearchInput
      placeholder={placeholder}
      autoCompleteKeywords={autoCompleteKeywords}
    />
    <br />
    <DropdownInput dropdownKeywords={dropdownKeywords} />
  </div>
);

export const Default = DefaultTemplate.bind({});
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
  dropdownKeywords: [
    2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
  ],
};
