import './NavBar.scss';

import { Link } from 'react-router-dom';
import Logo from '../../core/Logo/Logo';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import React from 'react';

const NavBar = () => {
  return (
    <div className='navigation-container'>
      <PageLayout>
        <Link to={PATH.HOME}>
          <Logo />
        </Link>
      </PageLayout>
    </div>
  );
};

export default NavBar;
