import './GameList.scss';

import { GameCard, SearchInput, Slider } from '../../components';
import React, { useEffect, useRef, useState } from 'react';

import { Headline2 } from '../../core/Typography';
import { Link } from 'react-router-dom';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import axios from 'axios';
import getKorRegExp from '../../components/SearchInput/service/getKorRegExp';

const GameList = () => {
  // const [sliderImageList, setSliderImageList] = useState([]);
  const [gameList, setGameList] = useState([]);
  const [selectedGames, setSelectedGames] = useState([]);
  const searchRef = useRef(null);

  const dummyImage = [
    {
      id: 0,
      info: 'Kartrider Rush Plus',
      src: 'https://babble.gg/img/banners/kartrider.png',
    },
    {
      id: 1,
      info: 'APEX LEGENDS',
      src: 'https://babble.gg/img/banners/apexlegends.png',
    },
    {
      id: 2,
      info: 'Fortnite',
      src: 'https://babble.gg/img/banners/fortnite.png',
    },
  ];

  const selectGame = (gameName) => {
    const game = gameList.find(({ name }) => name === gameName);
    setSelectedGames([game]);
  };

  // const getSliderImages = async () => {
  //   const response = await axios.get('');
  //   const images = response.data;

  //   setSliderImageList(images);
  // };

  const getGames = async () => {
    const response = await axios.get('https://test-api.babble.gg/api/games');
    const games = response.data;

    setGameList(games);
    setSelectedGames(games);
  };

  const onChangeGameInput = (e) => {
    const inputValue = e.target.value.replace(
      /[^0-9|ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z|A-Z]+/g,
      ''
    );

    const searchResults = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+/g.test(inputValue)
      ? gameList.filter((game) => {
          const keywordRegExp = getKorRegExp(inputValue, {
            initialSearch: true,
            ignoreSpace: true,
          });
          return game.name.match(keywordRegExp);
        })
      : gameList.filter((game) => {
          const searchRegex = new RegExp(inputValue, 'gi');
          const keywordWithoutSpace = game.name.replace(/\s/g, '');
          return (
            keywordWithoutSpace.match(searchRegex) ||
            game.name.match(searchRegex)
          );
        });

    setSelectedGames(searchResults);
  };

  useEffect(() => {
    const stickyObserver = new IntersectionObserver(
      ([entry]) => {
        entry.target.classList.toggle(
          'stuck',
          // entry.intersectionRatio <= 0.99 && !entry.isIntersecting
          entry.boundingClientRect.top <= 0 && !entry.isIntersecting
        );
      },
      { threshold: 1 }
    );
    stickyObserver.observe(searchRef.current);

    // getSliderImages();
    getGames();

    return () => {
      stickyObserver && stickyObserver.disconnect();
    };
  }, []);

  return (
    <div className='game-list-container'>
      <Slider imageList={dummyImage} />
      <PageLayout>
        <Headline2>전체 게임</Headline2>
        <div className='search-container' ref={searchRef}>
          <section className='search-section'>
            <SearchInput
              placeholder='게임을 검색해주세요.'
              autoCompleteKeywords={selectedGames.map(({ name }) => ({
                name,
              }))}
              onClickKeyword={selectGame}
              onChangeInput={onChangeGameInput}
              isResetable={false}
            />
          </section>
        </div>
        <div className='game-list'>
          {selectedGames.map(({ id, name, headCount, thumbnail }) => (
            <Link to={`${PATH.ROOM_LIST}/${id}`} key={id}>
              <GameCard
                gameName={name}
                imageSrc={thumbnail}
                participants={headCount}
              />
            </Link>
          ))}
        </div>
      </PageLayout>
    </div>
  );
};

export default GameList;
