import './ThemeMode.scss';

import React from 'react';
import useTheme from '../../hooks/useTheme';

const ThemeMode = () => {
  const { darkTheme, toggleDarkTheme } = useTheme();

  return (
    <div className='container'>
      <div className='theme-container'>
        <button onClick={toggleDarkTheme}>
          {darkTheme ? (
            <img src='https://babble.gg/img/icons/sunny.png' alt='light-mode' />
          ) : (
            <img src='https://babble.gg/img/icons/moon.png' alt='dark-mode' />
          )}
        </button>
      </div>
    </div>
  );
};

export default ThemeMode;
