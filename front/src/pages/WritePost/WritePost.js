import './WritePost.scss';

import { MainImage, ModalError } from '../../components';

import { BASE_URL } from '../../constants/api';
import { Headline2 } from '../../core/Typography';
import { Link } from 'react-router-dom';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import React from 'react';
import { WritingBlock } from '../../chunks';
import axios from 'axios';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';
import { useHistory } from 'react-router';

const WritePost = () => {
  const history = useHistory();
  const { openModal } = useDefaultModal();

  const submitPost = async (postObj) => {
    try {
      const response = await axios.post(`${BASE_URL}/api/post`, postObj);
      const postData = response.data;

      history.push(`${PATH.BOARD}${PATH.VIEW_POST}/${postData.id}`);
    } catch (error) {
      openModal(<ModalError>글 작성에 실패했습니다.</ModalError>);
    }
  };

  return (
    <main className='board-container'>
      <MainImage imageSrc={'https://babble.gg/img/background/board-main.jpg'} />
      <PageLayout>
        <div className='board-header'>
          <Link to={PATH.BOARD}>
            <Headline2>익명 게시판</Headline2>
          </Link>
        </div>
        <WritingBlock submitPost={submitPost} />
      </PageLayout>
    </main>
  );
};

export default WritePost;
