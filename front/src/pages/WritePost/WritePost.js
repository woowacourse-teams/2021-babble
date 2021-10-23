import './WritePost.scss';

import { Headline2 } from '../../core/Typography';
import { Link } from 'react-router-dom';
import { MainImage } from '../../components';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import React from 'react';
import { WritingBlock } from '../../chunks';

const WritePost = () => {
  return (
    <main className='board-container'>
      <MainImage imageSrc={'https://babble.gg/img/background/board-main.jpg'} />
      <PageLayout>
        <div className='board-header'>
          <Link to={PATH.BOARD}>
            <Headline2>익명 게시판</Headline2>
          </Link>
        </div>
        <WritingBlock />
      </PageLayout>
    </main>
  );
};

export default WritePost;
