import React, { createContext, useState } from 'react';

import PropTypes from 'prop-types';

export const ThemeChangeContext = createContext(null);

export const ThemeChangeContextProvider = ({ children }) => {
  const [darkTheme, setDarkTheme] = useState(false);

  return (
    <ThemeChangeContext.Provider value={{ darkTheme, setDarkTheme }}>
      {children}
    </ThemeChangeContext.Provider>
  );
};

ThemeChangeContextProvider.propTypes = {
  children: PropTypes.node,
};
