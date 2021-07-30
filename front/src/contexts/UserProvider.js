import React, { createContext, useContext, useState } from 'react';

import PropTypes from 'prop-types';

const UserContext = createContext();

const UserProvider = ({ children }) => {
  const [user, setUser] = useState({
    id: -1,
    nickname: '',
    avatar: '',
  });

  const changeUserId = (id) => {
    setUser((prevState) => ({ ...prevState, id }));
  };

  const changeNickname = (nickname) => {
    setUser((prevState) => ({ ...prevState, nickname }));
  };

  const changeAvatar = (avatarUrl) => {
    setUser((prevState) => ({ ...prevState, avatar: avatarUrl }));
  };

  return (
    <UserContext.Provider
      value={{ user, changeUserId, changeNickname, changeAvatar }}
    >
      {children}
    </UserContext.Provider>
  );
};

UserProvider.propTypes = {
  children: PropTypes.node,
};

export const useUser = () => {
  return useContext(UserContext);
};

export default UserProvider;
