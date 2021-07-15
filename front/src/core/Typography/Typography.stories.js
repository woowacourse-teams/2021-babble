import Body1 from './Body1';
import Body2 from './Body2';
import Caption1 from './Caption1';
import Caption2 from './Caption2';
import Headline1 from './Headline1';
import Headline2 from './Headline2';
import React from 'react';
import Subtitle1 from './Subtitle1';
import Subtitle2 from './Subtitle2';
import Subtitle3 from './Subtitle3';

export default {
  title: 'Typography/Typography',
  component: [
    Body1,
    Body2,
    Caption1,
    Caption2,
    Headline1,
    Headline2,
    Subtitle1,
    Subtitle2,
    Subtitle3,
  ],
  decorators: [(story) => <div style={{ padding: '3rem' }}>{story()}</div>],
};

const Headline1Template = (args) => <Headline1 {...args} />;
const Headline2Template = (args) => <Headline2 {...args} />;
const Subtitle1Template = (args) => <Subtitle1 {...args} />;
const Subtitle2Template = (args) => <Subtitle2 {...args} />;
const Subtitle3Template = (args) => <Subtitle3 {...args} />;
const Body1Template = (args) => <Body1 {...args} />;
const Body2Template = (args) => <Body2 {...args} />;
const Caption1Template = (args) => <Caption1 {...args} />;
const Caption2Template = (args) => <Caption2 {...args} />;

const Default = (args) => (
  <div>
    <Headline1Template bold>{args.headline1Bold}</Headline1Template>
    <Headline2Template bold>{args.headline2Bold}</Headline2Template>
    <br />
    <Headline1Template>{args.headline1}</Headline1Template>
    <Headline2Template>{args.headline2}</Headline2Template>
    <br />
    <Subtitle1Template>{args.subtitle1}</Subtitle1Template>
    <Subtitle2Template>{args.subtitle2}</Subtitle2Template>
    <Subtitle3Template>{args.subtitle3}</Subtitle3Template>
    <br />
    <Body1Template>{args.body1}</Body1Template>
    <Body2Template>{args.body2}</Body2Template>
    <br />
    <Caption1Template>{args.caption1}</Caption1Template>
    <br />
    <Caption2Template>{args.caption2}</Caption2Template>
  </div>
);

export const BabbleTypography = Default.bind({});

BabbleTypography.args = {
  headline1Bold: 'Headline1 Bold',
  headline1: 'Headline1',
  headline2Bold: 'Headline2 Bold',
  headline2: 'Headline2',
  subtitle1: 'Subtitle1',
  subtitle2: 'Subtitle2',
  subtitle3: 'Subtitle3',
  body1: 'Body1',
  body2: 'Body2',
  caption1: 'Caption1',
  caption2: 'Caption2',
};
