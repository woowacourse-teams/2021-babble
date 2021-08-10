import './Main.scss';

import PropTypes from 'prop-types';
import React from 'react';

const Main = ({ children }) => {
  return <main className='main-container'>{children}</main>;
};

Main.propTypes = {
  children: PropTypes.node,
};

export default Main;
