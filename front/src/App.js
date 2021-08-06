import '../global.scss';

import { Footer, NavBar } from './components';
import { Route, Switch } from 'react-router-dom';

import GameList from './pages/GameList/GameList';
import MakeRoom from './pages/MakeRoom/MakeRoom';
import PATH from './constants/path';
import React from 'react';
import RoomList from './pages/RoomList/RoomList';

const App = () => {
  return (
    <>
      <NavBar />
      <Switch>
        <Route path={PATH.HOME} component={GameList} exact />
        <Route
          path={`${PATH.ROOM_LIST}/:gameId${PATH.MAKE_ROOM}`}
          component={MakeRoom}
        />
        <Route path={`${PATH.ROOM_LIST}/:gameId`} component={RoomList} />
      </Switch>
      <Footer />
    </>
  );
};

export default App;
