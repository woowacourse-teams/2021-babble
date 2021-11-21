import { getLocalStorage, setLocalStorage } from '../utils/storage';

import { ThemeChangeContext } from '../contexts/ThemeChangeProvider';
import { useContext } from 'react';

const useTheme = () => {
  const { darkTheme, setDarkTheme } = useContext(ThemeChangeContext);

  const toggleDarkTheme = () => {
    const isDarkMode = getLocalStorage('darkMode');
    setLocalStorage('darkMode', !isDarkMode);
    document.body.classList.toggle('dark-mode');
    setDarkTheme((wasDarkMode) => !wasDarkMode);
  };

  return { darkTheme, toggleDarkTheme };
};

export default useTheme;
