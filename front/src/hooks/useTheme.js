import { useContext, useEffect } from 'react';

import { ThemeChangeContext } from '../contexts/ThemeChangeProvider';

const useTheme = () => {
  const { darkTheme, setDarkTheme } = useContext(ThemeChangeContext);

  const toggleDarkTheme = () => {
    if (document.body.dataset.theme === 'dark-mode') {
      localStorage.removeItem('theme');
      document.body.removeAttribute('data-theme');
      setDarkTheme(false);
      return;
    }

    localStorage.setItem('theme', 'dark-mode');
    document.body.setAttribute('data-theme', 'dark-mode');
    setDarkTheme(true);
    return;
  };

  useEffect(() => {
    const getDarkModeFromLocalStorage = () => {
      if (localStorage.getItem('theme') === 'dark-mode') {
        document.body.setAttribute('data-theme', 'dark-mode');
        setDarkTheme(true);
      }
    };

    getDarkModeFromLocalStorage();
  }, [setDarkTheme]);

  return { darkTheme, toggleDarkTheme };
};

export default useTheme;
