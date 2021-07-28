import '../global.scss';

import { Route, Switch } from 'react-router-dom';

import MakeRoom from './pages/MakeRoom/MakeRoom';
import React from 'react';
import RoomList from './pages/RoomList/RoomList';

const App = () => {
  return (
    <Switch>
      <Route path='/make-room'>
        <MakeRoom />
      </Route>
      <Route path='/'>
        <RoomList />
      </Route>
    </Switch>
  );
};

export default App;
