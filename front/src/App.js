import '../global.scss';

import { Route, Switch } from 'react-router-dom';

import MakeRoom from './pages/MakeRoom/MakeRoom';
import PATH from './constants/path';
import React from 'react';
import RoomList from './pages/RoomList/RoomList';

const App = () => {
  return (
    <Switch>
      <Route path={PATH.HOME} exact>
        <RoomList />
      </Route>
      <Route path={PATH.MAKE_ROOM}>
        <MakeRoom />
      </Route>
    </Switch>
  );
};

export default App;
