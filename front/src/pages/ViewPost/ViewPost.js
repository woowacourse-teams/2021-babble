import './ViewPost.scss';

import { Body2, Headline2, Subtitle2 } from '../../core/Typography';
import { MainImage, SquareButton } from '../../components';
import React, { useEffect, useState } from 'react';

import { AiOutlineLike } from '@react-icons/all-files/ai/AiOutlineLike';
import { BiTimeFive } from '@react-icons/all-files/bi/BiTimeFive';
import { BsPersonFill } from '@react-icons/all-files/bs/BsPersonFill';
import Darass from 'darass-react';
import InfoWithIcon from '../../components/InfoWithIcon/InfoWithIcon';
import LikeAndView from '../../chunks/LikeAndView/LikeAndView';
import { Link } from 'react-router-dom';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import PropTypes from 'prop-types';

const ViewPost = ({ match }) => {
  const { postId } = match.params;

  const [post, setPost] = useState({
    title: '',
    content: '',
    category: '',
    author: '',
    view: 0,
    like: 0,
    createdAt: '',
  });

  const getPost = async () => {
    // const response = await axios.get(`${postId}`);
    // const post = response.data;
    console.log(postId);
    setPost({
      title: '안녕하세용',
      content: '모더나 2차 아파용',
      category: '인사',
      author: '피터',
      view: 10,
      like: 5,
      createdAt: '2021-10-23',
    });
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
                content={post.author}
              />
              <InfoWithIcon
                icon={<BiTimeFive size='15px' />}
                content={post.createdAt}
              />
            </div>
          </div>
          <div className='post-body'>
            <div className='content'>{post.content}</div>
            <div className='like-button'>
              <SquareButton type='button' colored={false} name='like'>
                <AiOutlineLike size='24px' />
                {post.like}
              </SquareButton>
            </div>
          </div>
        </div>
        <Darass
          projectKey='ydF498CNZ2A6oxk193'
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
