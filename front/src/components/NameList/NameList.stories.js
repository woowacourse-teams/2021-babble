import NameList from './NameList';
import React from 'react';

export default {
  title: 'components/NameList',
  component: NameList,
};

const NameListTemplate = (args) => <NameList {...args} />;

export const Default = NameListTemplate.bind({});

Default.args = {
  list: [
    'League of Legends',
    'Battlegrounds',
    'Overwatch',
    'Apex Legends',
    'Sudden Attack',
    'Valorant',
    'Kartrider Rush +',
    'Minecraft',
    'Hearthstone',
    'Tekken 7',
    'Maplestory',
    'Maplestory2',
    'Escape from Tarkov',
    'Monster Hunter Iceborne',
    'Genshin Impact',
    'VR Chat',
    'Beat Saber',
    'Super Mario Maker',
    'Pokemon Unite',
    'Unrailed',
    'Fortnite',
    'Lost Ark',
    'Diablo 2 Resurrected',
    'Diablo 3',
    'Starcraft',
    'Starcraft 2',
    'Warcraft 3',
    'FIFA 22',
    'Teamfight Tactics',
    'Dead by Daylight',
    'Soulworker',
    'World of Warcraft',
    'Grand Theft Auto 5',
    'Roblox',
  ],
  erasable: true,
  onClickNames: () => console.log('Tag name clicked!'),
};
