import './NavBar.scss';

import { Link } from 'react-router-dom';
import Logo from '../../core/Logo/Logo';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import React from 'react';
import { Subtitle3 } from '../../core/Typography';

const NavBar = () => {
  return (
    <div className='navigation-container'>
      <PageLayout direction='row'>
        <Link to={PATH.HOME}>
          <Logo />
        </Link>
        <div className='nav-menu'>
          <Link to={PATH.HOME}>
            <Subtitle3>게임 매칭</Subtitle3>
          </Link>
          <Link to={PATH.BOARD}>
            <Subtitle3>익명 게시판</Subtitle3>
          </Link>
        </div>
      </PageLayout>
    </div>
  );
};

export default NavBar;
