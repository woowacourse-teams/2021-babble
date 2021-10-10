import './KakaoShareButton.scss';

import React, { useEffect } from 'react';

import PropTypes from 'prop-types';

const KakaoShareButton = ({ gameId, roomId }) => {
  useEffect(() => {
    createKakaoButton();
  }, []);

  const createKakaoButton = () => {
    if (window.Kakao) {
      const kakao = window.Kakao;

      if (!kakao.isInitialized()) {
        // TODO: kakao.init(process.env.REACT_APP_KAKAO_KEY);
        kakao.init('a6abc5f71c7dabdd128d08f6de040ba1');
      }

      kakao.Link.createCustomButton({
        container: '#kakao-link-button',
        templateId: 63115,
        templateArgs: {
          gameId,
          roomId,
        },
      });
    }
  };

  return (
    <div className='kakao-share-button'>
      <span className='share-button'>
        <button id='kakao-link-button'>
          <img
            src='https://babble.gg/img/icons/kakao-talk.png'
            alt='kakao button'
          />
        </button>
      </span>
    </div>
  );
};

KakaoShareButton.propTypes = {
  gameId: PropTypes.number,
  roomId: PropTypes.number,
};

export default KakaoShareButton;
