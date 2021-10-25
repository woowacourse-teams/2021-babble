import './Footer.scss';

import { BABBLE_URL } from '../../constants/api';
import { Caption2 } from '../../core/Typography';
import React from 'react';

const Footer = () => {
  return (
    <footer>
      <span className='footer-logo-image'>
        <img
          src={`${BABBLE_URL}/img/logos/footer_logo.png`}
          alt='footer logo'
          width='30px'
          height='30px'
        />
      </span>
      <div className='goal'>
        <Caption2>
          Babble은 게임 종류에 관계없이
          <br />
          모든 게이머들이 하나로 연결될 수 있는 플랫폼을 지향합니다.
        </Caption2>
      </div>
      <div className='company'>
        <span className='name'>Babble</span>
        <span className='separator'>|</span>
        <span className='ceo'>대표자 Hyun Cheol An, CEO</span>
      </div>
      <span>서울특별시 송파구 올림픽로35다길 42 루터회관</span>
      <span> mlch1603@naver.com </span>
      <span className='copyright'>
        Copyright &copy; 2021 Babble All Rights Reserved
      </span>
    </footer>
  );
};

export default Footer;
