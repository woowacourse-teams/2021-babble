import GameManagement from './GameManagement';
import React from 'react';

export default {
  title: 'chunks/GameManagement',
  component: GameManagement,
};

const GameManagementTemplate = (args) => (
  <div style={{ margin: '2rem' }}>
    <GameManagement {...args} />
  </div>
);

export const Default = GameManagementTemplate.bind({});

Default.args = {};
