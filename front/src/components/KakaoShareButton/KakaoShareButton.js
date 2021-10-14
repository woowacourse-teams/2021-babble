import './KakaoShareButton.scss';

import React, { useEffect } from 'react';

import { ModalError } from '../Modal/ModalError';
import PropTypes from 'prop-types';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const KakaoShareButton = ({ gameId, roomId }) => {
  const { openModal } = useDefaultModal();

  useEffect(() => {
    createKakaoButton();
  }, []);

  const createKakaoButton = () => {
    if (!window.Kakao) {
      openModal(<ModalError>공유하기 오류입니다.</ModalError>);
      return;
    }

    const kakao = window.Kakao;

    if (!kakao.isInitialized()) {
      kakao.init(process.env.REACT_APP_KAKAO_KEY);
    }

    kakao.Link.createCustomButton({
      container: '#kakao-link-button',
      templateId: 63115,
      templateArgs: {
        gameId,
        roomId,
      },
    });
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
