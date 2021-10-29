import '../style/global.scss';
import '../style/fonts.scss';

import { Footer, Main, ModalError, NavBar } from './components';
import React, { useEffect } from 'react';
import { Route, Switch } from 'react-router-dom';
import { getSessionStorage, setSessionStorage } from './utils/storage';

import { BASE_URL } from './constants/api';
import PATH from './constants/path';
import axios from 'axios';
import { getRandomNickname } from '@woowa-babble/random-nickname';
import loadable from '@loadable/component';
import { useDefaultModal } from './contexts/DefaultModalProvider';
import useScript from './hooks/useScript';
import { useUser } from './contexts/UserProvider';

const GameList = loadable(() => import('./pages/GameList/GameList'));
const NotFound = loadable(() => import('./components/NotFound/NotFound'));
const MakeRoom = loadable(() => import('./pages/MakeRoom/MakeRoom'));
const RoomList = loadable(() => import('./pages/RoomList/RoomList'));
const Board = loadable(() => import('./pages/Board/Board'));
const ViewPost = loadable(() => import('./pages/ViewPost/ViewPost'));
const WritePost = loadable(() => import('./pages/WritePost/WritePost'));
const BabbleManagement = loadable(() =>
  import('./pages/BabbleManagement/BabbleManagement')
);

const App = () => {
  const { changeUser } = useUser();
  const { openModal } = useDefaultModal();

  useScript('https://developers.kakao.com/sdk/js/kakao.js');

  const getUserId = async () => {
    const newUser = { id: -1, nickname: '' };
    newUser.nickname = getSessionStorage('nickname');

    if (!newUser.nickname) {
      newUser.nickname = `${getRandomNickname('characters')}`;
      setSessionStorage('nickname', newUser.nickname);
    }

    try {
      const response = await axios.post(`${BASE_URL}/api/users`, {
        nickname: newUser.nickname,
      });

      newUser.id = response.data.id;
      newUser.nickname = response.data.nickname;

      changeUser(newUser);
    } catch (error) {
      openModal(<ModalError>{error.response?.data?.message}</ModalError>);
    }
  };

  useEffect(() => {
    getUserId();
  }, []);

  return (
    <>
      <NavBar />
      <Main>
        <Switch>
          <Route path={PATH.HOME} component={GameList} exact />
          <Route
            path={`${PATH.ROOM_LIST}/:gameId${PATH.MAKE_ROOM}`}
            component={MakeRoom}
          />
          <Route
            path={`${PATH.ROOM_LIST}/:gameId`}
            component={RoomList}
            exact
          />
          <Route
            path={`${PATH.ROOM_LIST}/:gameId/chat/:roomId`}
            component={RoomList}
            exact
          />
          <Route path={PATH.ADMIN} component={BabbleManagement} exact />
          <Route path={PATH.BOARD} component={Board} exact />
          <Route
            path={`${PATH.BOARD}${PATH.WRITE_POST}`}
            component={WritePost}
            exact
          />
          <Route
            path={`${PATH.BOARD}${PATH.VIEW_POST}/:postId`}
            component={ViewPost}
            exact
          />
          <Route component={NotFound} />
        </Switch>
      </Main>
      <Footer />
    </>
  );
};

export default App;
