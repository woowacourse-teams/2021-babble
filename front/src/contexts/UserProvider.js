import React, { createContext, useContext, useState } from 'react';

import PropTypes from 'prop-types';

const UserContext = createContext();

const UserProvider = ({ children }) => {
  // TODO: 데모데이 이후 isNicknameChanged 삭제하고 더 나은 방법 찾아보기
  const [isNicknameChanged, setIsNicknameChanged] = useState(false);
  const [user, setUser] = useState({
    id: -1,
    nickname: '',
    avatar: '',
  });

  const changeUser = (user) => {
    setUser((prevState) => ({ ...prevState, ...user }));
  };

  return (
    <UserContext.Provider
      value={{
        user,
        changeUser,
        isNicknameChanged,
        setIsNicknameChanged,
      }}
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
