import './Board.scss';

import { Body2, Headline2 } from '../../core/Typography';
import { DropdownInput, MainImage, SquareButton } from '../../components';
import React, { useEffect, useState } from 'react';

import { BASE_URL } from '../../constants/api';
import PageLayout from '../../core/Layout/PageLayout';
import TableContent from '../../chunks/TableContent/TableContent';
import TextSearchInput from '../../components/SearchInput/TextSearchInput';
import axios from 'axios';

const Board = () => {
  const [category, setCategory] = useState('');

  //TODO: Dummy Data -> 나중에 고칠 예정
  const [boards, setBoards] = useState([
    {
      id: 1,
      title: '롤드컵 존잼 ;; ㄷㄷ',
      category: '자유',
      viewCount: '12',
      likeCount: '12',
      author: '그룸밍',
      createdAt: '10/23 09:34',
      notice: true,
    },
    {
      id: 2,
      title: '롤드컵 존잼 ;; ㄷㄷ',
      category: '자유',
      viewCount: '12',
      likeCount: '12',
      author: '그룸밍',
      createdAt: '10/23 09:34',
      notice: false,
    },
    {
      id: 3,
      title: '롤드컵 존잼 ;; ㄷㄷ',
      category: 'Monster Hunter Iceborne',
      viewCount: '12',
      likeCount: '12',
      author: '그룸밍',
      createdAt: '10/23 09:34',
      notice: true,
    },
  ]);

  const getBoards = async () => {
    // TODO: API 확정되면 Dummy Data와 함께 맞추어 고칠 예정
    // const response = await axios.get(`${BASE_URL}/board`);
    // setBoards(response.data);
  };

  useEffect(() => {
    getBoards();
  }, []);

  return (
    <main className='board-container'>
      <MainImage imageSrc={'https://babble.gg/img/background/board-main.jpg'} />
      <PageLayout>
        <div className='board-header'>
          <Headline2>익명 게시판</Headline2>
          <SquareButton>
            <Body2>글 쓰기</Body2>
          </SquareButton>
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
          {boards.map((board) => (
            <div className='content-wrapper' key={board.id}>
              <TableContent
                boardDetails={board}
                onTitleClick={() => {}}
                onCategoryClick={() => {}}
              />
            </div>
          ))}
        </div>
      </PageLayout>
    </main>
  );
};

export default Board;
