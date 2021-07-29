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
        {/* TODO: 나중에 gameId를 선택하는 게임에 따라 바뀌도록 로직 추가하기 */}
        <MakeRoom gameId={0} />
      </Route>
    </Switch>
  );
};

export default App;
