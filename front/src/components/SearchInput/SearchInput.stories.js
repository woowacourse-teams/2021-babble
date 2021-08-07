import DropdownInput from './DropdownInput';
import React from 'react';
import SearchInput from './SearchInput';
import { Subtitle3 } from '../../core/Typography';
import TextInput from './TextInput';

export default {
  title: 'components/SearchInput',
  component: [SearchInput, DropdownInput, TextInput],
};

const DefaultTemplate = ({ autoCompleteKeywords, dropdownKeywords }) => (
  <div>
    <Subtitle3>Auto Complete Search Input</Subtitle3>
    <br />
    <SearchInput autoCompleteKeywords={autoCompleteKeywords} />
    <br />
    <br />
    <Subtitle3>Dropdown Input</Subtitle3>
    <br />
    <DropdownInput dropdownKeywords={dropdownKeywords} />
    <br />
    <br />
    <Subtitle3>Text Input</Subtitle3>
    <br />
    <TextInput />
  </div>
);

export const Default = DefaultTemplate.bind({});
Default.args = {
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
