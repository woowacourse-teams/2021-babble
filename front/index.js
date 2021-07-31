import './global.scss';

import App from './src/App';
import ChattingModalProvider from './src/contexts/ChattingModalProvider';
import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import UserProvider from './src/contexts/UserProvider';

ReactDOM.render(
  <UserProvider>
    <ChattingModalProvider>
      <Router>
        <App />
      </Router>
    </ChattingModalProvider>
  </UserProvider>,
  document.getElementById('root')
);
