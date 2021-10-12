import './BabbleManagement.scss';

import { AdminManagement, GameManagement, TagManagement } from '../../chunks';

import { Headline1 } from '../../core/Typography';
import React from 'react';

const BabbleManagement = () => {
  return (
    <main className='management-container'>
      <Headline1>관리자 페이지</Headline1>
      <AdminManagement />
      <GameManagement />
      <TagManagement />
    </main>
  );
};

export default BabbleManagement;
