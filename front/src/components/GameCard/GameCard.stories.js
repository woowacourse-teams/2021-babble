import GameCard from './GameCard';
import React from 'react';

export default {
  title: 'components/GameCard',
  component: GameCard,
};

const GameCardTemplate = (args) => (
  <div style={{ width: '200px' }}>
    <GameCard {...args} />
  </div>
);

export const Default = GameCardTemplate.bind({});
Default.args = {
  gameName: 'League of Legends',
  imageSrc: 'https://images.igdb.com/igdb/image/upload/t_cover_big/co254s.jpg',
  participants: 1000,
};
