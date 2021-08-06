import '../global.scss';

import { INITIAL_VIEWPORTS } from '@storybook/addon-viewport';
import React from 'react';
import { addParameters } from '@storybook/react';

// import { addons } from '@storybook/addons';
// import theme from './theme';

export const decorators = [(Story) => <Story />];

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
