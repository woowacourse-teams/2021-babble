import './GameCard.scss';

import { Body2, Caption1 } from '../../core/Typography';
import React, { useRef } from 'react';

import PropTypes from 'prop-types';

const GameCard = ({ gameName, imageSrc, participants, ...rest }) => {
  const thumbnailRef = useRef(null);

  const onImageMouseDown = () => {
    thumbnailRef.current.classList.add('active');
  };

  const onImageMouseUp = () => {
    thumbnailRef.current.classList.remove('active');
  };

  const onImageMouseLeave = () => {
    thumbnailRef.current.classList.remove('center', 'right', 'left', 'active');
  };

  const onImageMouseMove = (e) => {
    thumbnailRef.current.classList.remove('center', 'right', 'left', 'active');

    const leftOffset = thumbnailRef.current.getBoundingClientRect().left;
    const imageWidth = thumbnailRef.current.offsetWidth;
    const myPosX = e.pageX;
    const clearedClassList = thumbnailRef.current.className
      .replace(/center|right|left/gi, '')
      .trim();

    if (myPosX < leftOffset + 0.3 * imageWidth) {
      thumbnailRef.current.className = `${clearedClassList} left`;
    } else {
      if (myPosX > leftOffset + 0.65 * imageWidth) {
        thumbnailRef.current.className = `${clearedClassList} right`;
      } else {
        thumbnailRef.current.className = `${clearedClassList} center`;
      }
    }
  };

  return (
    <figure className='game-card-container' {...rest}>
      <div
        className='thumbnail'
        ref={thumbnailRef}
        onMouseDown={onImageMouseDown}
        onMouseUp={onImageMouseUp}
        onMouseMove={onImageMouseMove}
        onMouseOut={onImageMouseLeave}
      >
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
