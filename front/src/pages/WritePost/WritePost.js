import './WritePost.scss';

import { Body2, Headline2, Subtitle1 } from '../../core/Typography';
import {
  DropdownInput,
  MainImage,
  ModalError,
  SquareButton,
} from '../../components';
import React, { useEffect, useState } from 'react';

import { BASE_URL } from '../../constants/api';
import { Link } from 'react-router-dom';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import { WritingBlock } from '../../chunks';
import axios from 'axios';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';
import { useHistory } from 'react-router';

const WritePost = () => {
  const history = useHistory();
  const { openModal } = useDefaultModal();

  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');

  const submitPost = async (e) => {
    e.preventDefault();

    const form = e.currentTarget.form;
    const title = form.title.value;
    const content = form.querySelector('.ql-editor').innerHTML;
    const nickname = form.nickname.value;
    const password = form.password.value;

    const post = {
      title,
      content,
      nickname,
      password,
      category: selectedCategory || '자유',
    };

    try {
      const response = await axios.post(`${BASE_URL}/api/post`, post);
      const postData = response.data;

      history.push(`${PATH.BOARD}${PATH.VIEW_POST}/${postData.id}`);
    } catch (error) {
      openModal(<ModalError>글 작성에 실패했습니다.</ModalError>);
    }
  };

  useEffect(() => {
    setCategories(['자유', '건의', '게임', '공지']);
  }, []);

  return (
    <main className='board-container'>
      <MainImage imageSrc={'https://babble.gg/img/background/board-main.jpg'} />
      <PageLayout>
        <div className='board-header'>
          <Link to={PATH.BOARD}>
            <Headline2>익명 게시판</Headline2>
          </Link>
        </div>
        <section className='writing-section'>
          <div className='writing-header'>
            <Subtitle1>게시글 작성</Subtitle1>
            <div className='category'>
              <DropdownInput
                type='text'
                placeholder='카테고리를 선택하세요.'
                dropdownKeywords={categories}
                inputValue={selectedCategory}
                setInputValue={setSelectedCategory}
              />
            </div>
          </div>
          <form className='writing-form'>
            <WritingBlock />
            <SquareButton
              type='submit'
              size='block'
              name='write'
              onClickButton={submitPost}
            >
              <Body2>작성하기</Body2>
            </SquareButton>
          </form>
        </section>
      </PageLayout>
    </main>
  );
};

export default WritePost;
