import './Board.scss';

import { Body2, Headline2 } from '../../core/Typography';
import { DropdownInput, MainImage, SquareButton } from '../../components';
import React, { useEffect, useState } from 'react';

import { Link } from 'react-router-dom';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import TableContent from '../../chunks/TableContent/TableContent';
import TextSearchInput from '../../components/SearchInput/TextSearchInput';

// import { BASE_URL } from '../../constants/api';

// import axios from 'axios';

const Board = () => {
  const [category, setCategory] = useState('');

  //TODO: Dummy Data -> 나중에 고칠 예정
  const [posts, setPosts] = useState([]);

  const getPosts = async () => {
    // TODO: API 확정되면 Dummy Data와 함께 맞추어 고칠 예정
    // const response = await axios.get(`${BASE_URL}/board`);
    setPosts([
      {
        id: 1,
        title: '롤드컵 존잼 ;; ㄷㄷ',
        category: '자유',
        viewCount: 12,
        likeCount: 12,
        author: '그룸밍',
        createdAt: '10/23 09:34',
        notice: true,
      },
      {
        id: 2,
        title: '롤드컵 존잼 ;; ㄷㄷ',
        category: '자유',
        viewCount: 12,
        likeCount: 12,
        author: '그룸밍',
        createdAt: '10/23 09:34',
        notice: false,
      },
      {
        id: 3,
        title: '롤드컵 존잼 ;; ㄷㄷ',
        category: 'Monster Hunter Iceborne',
        viewCount: 12,
        likeCount: 12,
        author: '그룸밍',
        createdAt: '10/23 09:34',
        notice: true,
      },
    ]);
  };

  useEffect(() => {
    getPosts();
  }, []);

  return (
    <main className='board-container'>
      <MainImage
        imageSrc={'https://babble.gg/img/background/board-main-reduced.jpg'}
      />
      <PageLayout>
        <div className='board-header'>
          <Link to={PATH.BOARD}>
            <Headline2>익명 게시판</Headline2>
          </Link>
          <Link to={`${PATH.BOARD}${PATH.WRITE_POST}`}>
            <SquareButton>
              <Body2>글 쓰기</Body2>
            </SquareButton>
          </Link>
        </div>
        <div className='board-search'>
          <TextSearchInput />
          <DropdownInput
            type='text'
            placeholder=''
            defaultInputValue='전체'
            inputValue={category}
            setInputValue={setCategory}
            dropdownKeywords={['전체', '글 + 제목', '글쓴이']}
          />
        </div>
        <div className='board-wrapper'>
          {posts.map((post) => (
            <Link
              to={`${PATH.BOARD}${PATH.VIEW_POST}/${post.id}`}
              key={post.id}
            >
              <div className='content-wrapper'>
                <TableContent
                  boardDetails={post}
                  onTitleClick={() => {}}
                  onCategoryClick={() => {}}
                />
              </div>
            </Link>
          ))}
        </div>
      </PageLayout>
    </main>
  );
};

export default Board;
