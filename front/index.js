import './global.scss';

import App from './src/App';
import ChattingModalProvider from './src/contexts/ChattingModalProvider';
import DefaultModalProvider from './src/contexts/DefaultModalProvider';
import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import ScrollToTop from './src/components/ScrollToTop/ScrollToTop';
import UserProvider from './src/contexts/UserProvider';

ReactDOM.render(
  <UserProvider>
    <DefaultModalProvider>
      <ChattingModalProvider>
        <Router>
          <ScrollToTop />
          <App />
        </Router>
      </ChattingModalProvider>
    </DefaultModalProvider>
  </UserProvider>,
  document.getElementById('root')
);
