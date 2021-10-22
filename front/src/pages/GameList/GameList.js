import './GameList.scss';

import { BABBLE_URL, BASE_URL } from '../../constants/api';
import { GameCard, ModalError, SearchInput, Slider } from '../../components';
import React, { useEffect, useRef, useState } from 'react';

import { Headline2 } from '../../core/Typography';
import { Link } from 'react-router-dom';
import PATH from '../../constants/path';
import { PATTERNS } from '../../constants/regex';
import PageLayout from '../../core/Layout/PageLayout';
import axios from 'axios';
import getKorRegExp from '../../components/SearchInput/service/getKorRegExp';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const GameList = () => {
  // const [sliderImageList, setSliderImageList] = useState([]);
  const [gameList, setGameList] = useState([]);
  const [selectedGames, setSelectedGames] = useState([]);
  const searchRef = useRef(null);

  const { openModal } = useDefaultModal();

  const dummyImage = [
    {
      id: 0,
      info: 'Kartrider Rush Plus',
      src: `${BABBLE_URL}/img/banners/kartrider.png`,
    },
    {
      id: 1,
      info: 'APEX LEGENDS',
      src: `${BABBLE_URL}/img/banners/apexlegends.png`,
    },
    {
      id: 2,
      info: 'Fortnite',
      src: `${BABBLE_URL}/img/banners/fortnite.png`,
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
    try {
      const response = await axios.get(`${BASE_URL}/api/games`);
      const games = response.data;

      setGameList(games);
      setSelectedGames(games);
    } catch (error) {
      openModal(<ModalError>{error.message}</ModalError>);
    }
  };

  const onChangeGameInput = (e) => {
    const inputValue = e.target.value.replace(PATTERNS.SPECIAL_CHARACTERS, '');

    const searchResults = PATTERNS.KOREAN.test(inputValue)
      ? gameList.filter((game) => {
          const searchRegex = getKorRegExp(inputValue, {
            initialSearch: true,
            ignoreSpace: true,
          });

          return game.name.match(searchRegex);
        })
      : gameList.filter((game) => {
          const searchRegex = new RegExp(inputValue, 'gi');
          const nameWithoutSpace = game.name.replace(PATTERNS.SPACE, '');

          return (
            nameWithoutSpace.match(searchRegex) || game.name.match(searchRegex)
          );
        });

    // 대안 이름으로 검색된 게임
    const alternativeResults = PATTERNS.KOREAN.test(inputValue)
      ? gameList.filter((game) => {
          const searchRegex = getKorRegExp(inputValue, {
            initialSearch: true,
            ignoreSpace: true,
          });

          return game.alternativeNames.some((alternativeName) =>
            alternativeName.name.match(searchRegex)
          );
        })
      : gameList.filter((game) => {
          const searchRegex = new RegExp(inputValue, 'gi');
          const alternativeNamesWithoutSpace = game.alternativeNames.map(
            (alternativeName) =>
              alternativeName.name.replace(PATTERNS.SPACE, '')
          );

          return (
            alternativeNamesWithoutSpace.some((alternativeName) =>
              alternativeName.match(searchRegex)
            ) ||
            game.alternativeNames.some((alternativeName) =>
              alternativeName.name.match(searchRegex)
            )
          );
        });

    const result = new Set([...searchResults, ...alternativeResults]);
    setSelectedGames([...result]);
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

    return () => stickyObserver && stickyObserver.disconnect();
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
          {selectedGames.map(({ id, name, headCount, images }) => (
            <Link to={`${PATH.ROOM_LIST}/${id}`} key={id}>
              <GameCard
                gameName={name}
                imageSrc={images[2] ?? images[0]}
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
