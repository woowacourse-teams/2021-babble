import { Caption2, Subtitle3 } from '../../core/Typography';

import Avatar from '../Avatar/Avatar';
import AvatarWithBadge from '../Avatar/AvatarWithBadge';
import Badge from './Badge';
import React from 'react';
import TagErasable from '../Tag/TagErasable';

export default {
  title: 'components/Badge',
  component: Badge,
};

const BadgeTemplate = () => (
  <div style={{ width: '25rem' }}>
    <Subtitle3>태그와 함께 사용되는 배지</Subtitle3>
    <br />
    <div style={{ display: 'flex', justifyContent: 'space-between' }}>
      <Avatar direction='row'>
        <Caption2>hyun9mac</Caption2>
      </Avatar>
      <Badge>
        <Caption2>ME</Caption2>
      </Badge>
    </div>
    <br />

    <AvatarWithBadge direction='row' badgeText='host' colored>
      <Caption2>peter</Caption2>
    </AvatarWithBadge>

    <br />
    <TagErasable>
      <Caption2>실버</Caption2>
    </TagErasable>
  </div>
);

export const Default = BadgeTemplate.bind({});

Default.args = {};
