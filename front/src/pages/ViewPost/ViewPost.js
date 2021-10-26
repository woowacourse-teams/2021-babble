import './ViewPost.scss';

import { Body2, Headline2, Subtitle2 } from '../../core/Typography';
import { Link, useHistory } from 'react-router-dom';
import { MainImage, ModalError, SquareButton } from '../../components';
import React, { useEffect, useState } from 'react';

import { AiOutlineLike } from '@react-icons/all-files/ai/AiOutlineLike';
import { BASE_URL } from '../../constants/api';
import { BiTimeFive } from '@react-icons/all-files/bi/BiTimeFive';
import { BsPersonFill } from '@react-icons/all-files/bs/BsPersonFill';
import Darass from 'darass-react';
import InfoWithIcon from '../../components/InfoWithIcon/InfoWithIcon';
import LikeAndView from '../../chunks/LikeAndView/LikeAndView';
import ModalAlert from '../../components/Modal/ModalAlert';
import ModalSubmit from '../../components/Modal/ModalSubmit';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import PropTypes from 'prop-types';
import ReactHtmlParser from 'react-html-parser';
import axios from 'axios';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const ViewPost = ({ match }) => {
  const { postId } = match.params;
  const history = useHistory();

  const { openModal, closeModal } = useDefaultModal();

  const [post, setPost] = useState({
    id: 0,
    title: '',
    content: '',
    category: '',
    nickname: '',
    view: 0,
    like: 0,
    createdDate: '',
    createdTime: '',
  });

  const getPost = async () => {
    try {
      const response = await axios.get(
        `${BASE_URL}/api${PATH.VIEW_POST}/${postId}`
      );
      const [createdDate, createdTime] = response.data.createdAt.split('T');
      const [parsedCreatedTime] = createdTime.split('.');
      setPost({
        ...response.data,
        createdDate,
        createdTime: parsedCreatedTime,
      });
    } catch (error) {
      console.error(error);
    }
  };

  const updatePost = async () => {
    history.push({
      pathname: `${PATH.BOARD}${PATH.WRITE_POST}`,
      state: {
        id: post.id,
        title: post.title,
        content: post.content,
        nickname: post.nickname,
        category: post.category,
      },
    });
  };

  const deletePost = async () => {
    const submitPassword = async (e) => {
      e.preventDefault();

      try {
        await axios.delete(`${BASE_URL}/api/post`, {
          data: {
            password: e.currentTarget.password.value,
            id: post.id,
          },
        });

        closeModal();
        openModal(<ModalAlert>게시글을 삭제했습니다.</ModalAlert>);

        history.push(`${PATH.BOARD}`);
      } catch (error) {
        closeModal();
        openModal(<ModalError>{error.response?.data?.message}</ModalError>);
      }
    };

    openModal(
      <ModalSubmit
        title='게시글 삭제하기'
        inputPlaceholder='비밀번호를 입력해주세요.'
        onSubmit={submitPassword}
        isSecret={true}
      />
    );
  };

  // TODO: 만료 기한 1일 쿠키로 1일 1회 제한하기
  const thumbsUp = async () => {
    const response = await axios.patch(
      `${BASE_URL}/api${PATH.VIEW_POST}/${post.id}/like`
    );
    setPost(response.data);
  };

  useEffect(() => {
    getPost();
  }, []);

  return (
    <main className='view-post-container'>
      <MainImage imageSrc={'https://babble.gg/img/background/board-main.jpg'} />
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
        <div className='post-wrapper'>
          <div className='post-header'>
            <div className='header-upper'>
              <Subtitle2>{post.title}</Subtitle2>
              <LikeAndView view={post.view} like={post.like} />
            </div>
            <div className='header-lower'>
              <InfoWithIcon
                icon={<BsPersonFill size='15px' />}
                content={post.nickname}
              />
              <InfoWithIcon
                icon={<BiTimeFive size='15px' />}
                content={
                  new Date(post.createdDate).getDate() >= new Date().getDate()
                    ? post.createdTime
                    : post.createdDate
                }
              />
            </div>
          </div>
          <div className='post-body'>
            <div className='content'>{ReactHtmlParser(post.content)}</div>
            <div className='like-button'>
              <SquareButton
                type='button'
                onClickButton={thumbsUp}
                colored={false}
                name='like'
              >
                <AiOutlineLike size='24px' />
                {post.like}
              </SquareButton>
            </div>
          </div>
        </div>
        <div className='buttons'>
          <SquareButton
            size='small'
            colored={false}
            name='modify'
            onClickButton={updatePost}
          >
            수정
          </SquareButton>
          <SquareButton size='small' name='delete' onClickButton={deletePost}>
            삭제
          </SquareButton>
        </div>
        <Darass
          projectKey={process.env.REACT_APP_DARASS_KEY}
          darkMode={false}
          primaryColor='#FF005C'
          isShowSortOption={true}
          isAllowSocialLogin={false}
          isShowLogo={false}
        />
      </PageLayout>
    </main>
  );
};

ViewPost.propTypes = {
  match: PropTypes.object,
};

export default ViewPost;
