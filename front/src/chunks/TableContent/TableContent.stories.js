import React from 'react';
import TableContent from './TableContent';

export default {
  title: 'chunks/TableContent',
  component: TableContent,
};

const TableContentTemplate = (args) => <TableContent {...args} />;

export const Default = TableContentTemplate.bind({});

Default.args = {
  boardDetails: {
    title: '롤드컵 존잼;; ㄷㄷ',
    category: '자유',
    author: '그루밍',
    createdAt: '10/18 09:23',
    viewCount: '18234',
    likeCount: '2918',
  },
};
