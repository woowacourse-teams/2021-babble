import '../global.scss';

import ChattingModalProvider from '../src/contexts/ChattingModalProvider';
import DefaultModalProvider from '../src/contexts/DefaultModalProvider';
import { INITIAL_VIEWPORTS } from '@storybook/addon-viewport';
import React from 'react';
import UserProvider from '../src/contexts/UserProvider';
import { addParameters } from '@storybook/react';

// import { addons } from '@storybook/addons';
// import theme from './theme';

export const decorators = [
  (Story) => (
    <UserProvider
      defaultValue={{
        id: -1,
        nickname: 'grooming',
        avatar: '',
        currentRoomNumber: -1,
      }}
    >
      <ChattingModalProvider>
        <DefaultModalProvider>
          <Story />
        </DefaultModalProvider>
      </ChattingModalProvider>
    </UserProvider>
  ),
];

export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};

addParameters({
  viewport: {
    defaultViewport: 'desktop',
    viewports: INITIAL_VIEWPORTS,
  },
});

// addons.setConfig({ theme: theme });
