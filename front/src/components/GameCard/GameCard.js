import './GameCard.scss';

import Body2 from '../../core/Typography/Body2';
import Caption1 from '../../core/Typography/Caption1';
import PropTypes from 'prop-types';
import React from 'react';

const GameCard = ({ gameName, imageSrc, participants, ...rest }) => {
  return (
    <figure className='game-card-container' {...rest}>
      <button className='thumbnail-button'>
        <img src={imageSrc} alt={`${gameName} image`} />
      </button>
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
