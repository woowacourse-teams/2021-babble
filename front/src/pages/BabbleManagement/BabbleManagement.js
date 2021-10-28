import './BabbleManagement.scss';

import { AdminManagement, GameManagement, TagManagement } from '../../chunks';

import React from 'react';

const BabbleManagement = () => {
  return (
    <main className='management-container'>
      <AdminManagement />
      <GameManagement />
      <TagManagement />
    </main>
  );
};

export default BabbleManagement;
