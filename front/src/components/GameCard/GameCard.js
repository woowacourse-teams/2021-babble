import './GameCard.scss';

import { Body2, Caption1 } from '../../core/Typography';

import PropTypes from 'prop-types';
import React from 'react';

const GameCard = ({ gameName, imageSrc, participants, ...rest }) => {
  return (
    <figure className='game-card-container' {...rest}>
      <div className='thumbnail'>
        <img src={imageSrc} alt={`${gameName} image`} />
      </div>
      <figcaption>
        <Body2>
          <b>{gameName}</b>
        </Body2>
        <Caption1>
          <span className='participant-count'>{`${participants}명 `}</span>참여
          중
        </Caption1>
      </figcaption>
    </figure>
  );
};

GameCard.propTypes = {
  gameName: PropTypes.string,
  imageSrc: PropTypes.string,
  participants: PropTypes.number,
};

export default GameCard;
