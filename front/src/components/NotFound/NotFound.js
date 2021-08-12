import './NotFound.scss';

import { Body2 } from '../../core/Typography';
import { Headline1 } from '../../core/Typography';
import React from 'react';
import { useHistory } from 'react-router-dom';

const NotFound = () => {
  const history = useHistory();
  console.log(history);

  return (
    <div className='not-found-container'>
      <div className='content'>
        <Headline1>Oops!</Headline1>

        <Body2>404 Not Found 에러 발생!</Body2>
        <Body2>
          {<span className='path'>{history.location.pathname}</span>}에 해당하는
          페이지를 찾지 못했어요.
        </Body2>
      </div>
      <img src='https://babble.gg/img/logos/not_found.png' alt='404 image' />
    </div>
  );
};

export default NotFound;
