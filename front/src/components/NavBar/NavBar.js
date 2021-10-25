import './NavBar.scss';

import { Body1 } from '../../core/Typography';
import { Link } from 'react-router-dom';
import Logo from '../../core/Logo/Logo';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import React from 'react';

const NavBar = () => {
  return (
    <div className='navigation-container'>
      <PageLayout direction='row'>
        <Link to={PATH.HOME}>
          <Logo />
        </Link>
        <div className='nav-menu'>
          <Link to={PATH.HOME}>
            <Body1>게임 매칭</Body1>
          </Link>
          <Link to={PATH.BOARD}>
            <Body1>익명 게시판</Body1>
          </Link>
        </div>
      </PageLayout>
    </div>
  );
};

export default NavBar;
