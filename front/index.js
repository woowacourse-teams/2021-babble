import './global.scss';

import App from './src/App';
import ModalProvider from './src/contexts/ModalProvider';
import React from 'react';
import ReactDOM from 'react-dom';

ReactDOM.render(
  <ModalProvider>
    <App />
  </ModalProvider>,
  document.getElementById('root')
);
