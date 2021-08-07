import { Caption2, Subtitle3 } from '../../core/Typography';

import Avatar from '../Avatar/Avatar';
import Badge from './Badge';
import React from 'react';
import { RiVipCrown2Fill } from 'react-icons/ri';
import { VscCircleFilled } from 'react-icons/vsc';

export default {
  title: 'components/Badge',
  component: Badge,
};

const BadgeTemplate = () => (
  <div style={{ width: '30rem' }}>
    <Subtitle3>Badges used with participants</Subtitle3>
    <br />
    <div style={{ display: 'flex' }}>
      <Avatar direction='row'>
        <Caption2>jason</Caption2>
      </Avatar>
      <Badge>
        <RiVipCrown2Fill size='12px' />
      </Badge>
    </div>
    <br />
    <div style={{ display: 'flex' }}>
      <Avatar direction='row'>
        <Caption2>wilder</Caption2>
      </Avatar>
      <Badge>
        <VscCircleFilled size='12px' />
      </Badge>
    </div>
    <br />
  </div>
);

export const Default = BadgeTemplate.bind({});

Default.args = {};
