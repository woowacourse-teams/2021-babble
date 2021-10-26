import './Board.scss';

import { Body2, Headline2 } from '../../core/Typography';
import {
  DropdownInput,
  MainImage,
  ModalError,
  SquareButton,
} from '../../components';
import { Link, useHistory } from 'react-router-dom';
import React, { useEffect, useState } from 'react';

import { BASE_URL } from '../../constants/api';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import TableContent from '../../chunks/TableContent/TableContent';
import TextSearchInput from '../../components/SearchInput/TextSearchInput';
import axios from 'axios';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const Board = () => {
  const history = useHistory();
  const { openModal } = useDefaultModal();

  const [category, setCategory] = useState('');
  const [posts, setPosts] = useState([]);

  const getPosts = async () => {
    try {
      const response = await axios.get(
        `${BASE_URL}/api${PATH.VIEW_POST}?page=1`
      );
      const postList = response.data;
      const parsedPostList = postList.map((post) => {
        const [createdDate, createdTime] = post.createdAt.split('T');
        const [parsedCreatedTime] = createdTime.split('.');
        return {
          ...post,
          createdDate,
          createdTime: parsedCreatedTime,
        };
      });

      setPosts(parsedPostList);
    } catch (error) {
      openModal(<ModalError>글 목록 조회를 실패했습니다.</ModalError>);
    }
  };

  const clickPost = (postId) => () => {
    history.push(`${PATH.BOARD}${PATH.VIEW_POST}/${postId}`);
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
          {posts.length ? (
            posts.map((post) => (
              <div className='content-wrapper' key={post.id}>
                <TableContent
                  boardDetails={post}
                  onContentClick={clickPost(post.id)}
                  onCategoryClick={() => {}}
                />
              </div>
            ))
          ) : (
            <div className='no-posts'>
              <Body2>게시글이 존재하지 않습니다.</Body2>
            </div>
          )}
        </div>
      </PageLayout>
    </main>
  );
};

export default Board;
