import Avatar from './Avatar';
import Badge from '../Badge/Badge';
import { Caption2 } from '../../core/Typography';
import PropTypes from 'prop-types';
import React from 'react';

const AvatarWithBadge = ({
  size = 'small',
  direction = 'col',
  imageSrc,
  badgeText,
  colored,
  children,
}) => {
  return (
    <div className='avatar-with-badge-container'>
      <Avatar size={size} direction={direction} imageSrc={imageSrc}>
        <Caption2>{children}</Caption2>
      </Avatar>
      <Badge colored={colored}>
        <Caption2>{badgeText.toUpperCase()}</Caption2>
      </Badge>
    </div>
  );
};

AvatarWithBadge.propTypes = {
  size: PropTypes.string,
  direction: PropTypes.string,
  imageSrc: PropTypes.string,
  badgeText: PropTypes.string,
  colored: PropTypes.bool,
  children: PropTypes.node,
};

export default AvatarWithBadge;
