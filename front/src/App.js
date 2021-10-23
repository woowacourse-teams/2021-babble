import '../style/global.scss';
import '../style/fonts.scss';

import { Footer, Main, NavBar } from './components';
import { Route, Switch } from 'react-router-dom';

import Board from './pages/Board/Board';
import PATH from './constants/path';
import React from 'react';
import ViewPost from './pages/ViewPost/ViewPost';
import WritePost from './pages/WritePost/WritePost';
import loadable from '@loadable/component';

const GameList = loadable(() => import('./pages/GameList/GameList'));
const NotFound = loadable(() => import('./components/NotFound/NotFound'));
const MakeRoom = loadable(() => import('./pages/MakeRoom/MakeRoom'));
const RoomList = loadable(() => import('./pages/RoomList/RoomList'));
const BabbleManagement = loadable(() =>
  import('./pages/BabbleManagement/BabbleManagement')
);

const App = () => {
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
