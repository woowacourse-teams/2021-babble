import React, { createContext, useContext, useEffect, useState } from 'react';

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

  const changeUserId = (id) => {
    setUser((prevState) => ({ ...prevState, id }));
  };

  const changeUserNickname = (nickname) => {
    setUser((prevState) => ({ ...prevState, nickname }));
  };

  const changeAvatar = (avatarUrl) => {
    setUser((prevState) => ({ ...prevState, avatar: avatarUrl }));
  };

  const generateSixDigits = () => {
    // 닉네임에만 쓰일 숫자(userId와 관련 없음) 익명#84729384
    // TODO: 지금은 숫자로 퉁치지만, 시간 나면 바로 형용사 + 명사 랜덤 매칭
    // { noun: '너구리', image: '너구리 사진' }
    // 라이브러리 npm 배포 가능
    return Math.floor(100000 + Math.random() * 90000);
  };

  useEffect(() => {
    // TODO: localStorage로 새로고침 후에도 닉네임 유지되도록 관리
    changeUserNickname(`익명#${generateSixDigits()}`);
  }, []);

  return (
    <UserContext.Provider
      value={{
        user,
        changeUserId,
        changeUserNickname,
        changeAvatar,
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
