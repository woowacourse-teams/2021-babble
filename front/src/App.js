import '../global.scss';

import { Footer, Main, NavBar } from './components';
import { Route, Switch } from 'react-router-dom';

import GameList from './pages/GameList/GameList';
import MakeRoom from './pages/MakeRoom/MakeRoom';
import NotFound from './components/ErrorBoundary/NotFound';
import PATH from './constants/path';
import React from 'react';
import RoomList from './pages/RoomList/RoomList';

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
          <Route path={`${PATH.ROOM_LIST}/:gameId`} component={RoomList} />
          <Route component={NotFound} />
        </Switch>
      </Main>
      <Footer />
    </>
  );
};

export default App;
