import './global.scss';

import App from './src/App';
import ModalProvider from './src/contexts/ModalProvider';
import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';

ReactDOM.render(
  <ModalProvider>
    <Router>
      <App />
    </Router>
  </ModalProvider>,
  document.getElementById('root')
);
