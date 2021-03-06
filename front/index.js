import App from './src/App';
import ChattingModalProvider from './src/contexts/ChattingModalProvider';
import DefaultModalProvider from './src/contexts/DefaultModalProvider';
import ErrorBoundary from './src/components/ErrorBoundary/ErrorBoundary';
import PushNotificationProvider from './src/contexts/PushNotificationProvider';
import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import ScrollToTop from './src/components/ScrollToTop/ScrollToTop';
import { ThemeChangeContextProvider } from './src/contexts/ThemeChangeProvider';
import UserProvider from './src/contexts/UserProvider';

ReactDOM.render(
  <ThemeChangeContextProvider>
    <UserProvider>
      <PushNotificationProvider>
        <DefaultModalProvider>
          <ChattingModalProvider>
            <Router>
              <ErrorBoundary>
                <ScrollToTop />
                <App />
              </ErrorBoundary>
            </Router>
          </ChattingModalProvider>
        </DefaultModalProvider>
      </PushNotificationProvider>
    </UserProvider>
  </ThemeChangeContextProvider>,
  document.getElementById('root')
);
