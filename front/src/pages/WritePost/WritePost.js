import './WritePost.scss';

import { Body2, Headline2, Subtitle1 } from '../../core/Typography';
import {
  DropdownInput,
  MainImage,
  ModalError,
  SquareButton,
} from '../../components';
import React, { useEffect, useState } from 'react';
import { useHistory, useLocation } from 'react-router';

import { BASE_URL } from '../../constants/api';
import { Link } from 'react-router-dom';
import ModalAlert from '../../components/Modal/ModalAlert';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import { WritingBlock } from '../../chunks';
import axios from 'axios';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const WritePost = () => {
  const history = useHistory();
  const location = useLocation();
  const { openModal } = useDefaultModal();

  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');

  const postToEdit = location.state;

  const submitPost = async (e) => {
    e.preventDefault();

    const form = e.currentTarget;
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
      openModal(<ModalError>{error.response?.data?.message}</ModalError>);
    }
  };

  const editPost = async (e) => {
    e.preventDefault();

    const form = e.currentTarget;
    const title = form.title.value;
    const content = form.querySelector('.ql-editor').innerHTML;
    const password = form.password.value;

    const post = {
      title,
      content,
      id: postToEdit.id,
      password,
      category: selectedCategory,
    };

    try {
      const response = await axios.put(`${BASE_URL}/api/post`, post);
      const postData = response.data;

      openModal(<ModalAlert>수정이 완료되었습니다.</ModalAlert>);
      history.push(`${PATH.BOARD}${PATH.VIEW_POST}/${postData.id}`);
    } catch (error) {
      openModal(<ModalError>{error.response?.data?.message}</ModalError>);
    }
  };

  useEffect(() => {
    setCategories(['자유', '건의', '게임', '공지']);

    if (postToEdit) {
      setSelectedCategory(postToEdit.category);
    }
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
            <Subtitle1>{postToEdit ? '게시글 수정' : '게시글 작성'}</Subtitle1>
            <div className='category'>
              <DropdownInput
                type='text'
                placeholder='카테고리를 선택하세요.'
                dropdownKeywords={categories}
                inputValue={selectedCategory}
                defaultInputValue={postToEdit?.category}
                setInputValue={setSelectedCategory}
              />
            </div>
          </div>
          <form
            className='writing-form'
            onSubmit={postToEdit ? editPost : submitPost}
          >
            <WritingBlock
              title={postToEdit?.title}
              content={postToEdit?.content}
              nickname={postToEdit?.nickname}
              textLimit={2600}
            />
            <SquareButton type='submit' size='block' name='write'>
              <Body2>{postToEdit ? '수정하기' : '작성하기'}</Body2>
            </SquareButton>
          </form>
        </section>
      </PageLayout>
    </main>
  );
};

export default WritePost;
