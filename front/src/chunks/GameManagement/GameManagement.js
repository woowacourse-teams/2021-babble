import './GameManagement.scss';

import { Body1, Headline2, Subtitle1, Subtitle3 } from '../../core/Typography';
import { ModalError, SquareButton, TextInput } from '../../components';
import React, { useEffect, useState } from 'react';

import { BASE_URL } from '../../constants/api';
import ImagePreview from '../../components/ImagePreview/ImagePreview';
import ImageRegister from '../../components/ImageRegister/ImageRegister';
import NameList from '../../components/NameList/NameList';
import axios from 'axios';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const GameManagement = () => {
  const [gameList, setGameList] = useState([]);
  const [selectedGame, setSelectedGame] = useState({
    name: '',
    imageList: [],
    alternativeNames: [],
  });

  const { openModal } = useDefaultModal();

  const getGameList = async () => {
    try {
      const response = await axios.get(`${BASE_URL}/api/games`);
      const games = response.data;

      setGameList(games);
    } catch (error) {
      openModal(<ModalError>{error}</ModalError>);
    }
  };

  const onSelectGame = ({ target }) => {
    const selectedGame = target.innerText;

    const selectedGameDetails = gameList.find(
      (game) => game.name === selectedGame
    );

    setSelectedGame((prevData) => ({
      ...prevData,
      name: selectedGame,
      // imageList: selectedGameDetails.thumbnail, TODO: thumbnail이 아직 List 형태가 아님
      imageList: [selectedGameDetails.thumbnail, '', ''],
      alternativeNames: selectedGameDetails.alternativeNames,
    }));
  };

  useEffect(() => {
    getGameList();
  }, []);

  return (
    <section className='game-management-container'>
      <Headline2>게임 관리</Headline2>
      <div className='game-management-wrapper indent'>
        <Subtitle1>게임 조회</Subtitle1>
        <NameList list={gameList} erasable onClickNames={onSelectGame} />

        <Subtitle1>게임 등록</Subtitle1>

        <form className='register-edit-game indent'>
          <Subtitle3>게임 이름</Subtitle3>
          <div className='game-name-input'>
            <TextInput value={selectedGame.name} placeholder='게임 이름' />
          </div>

          <ImageRegister />
          <ImagePreview imageList={selectedGame.imageList} />

          <Subtitle3>대체 이름(복수 개 가능)</Subtitle3>

          <div className='game-alternative-input'>
            <TextInput name='' maxLength='' placeholder='대체 이름' />
            <SquareButton size='block'>
              <Body1>등록</Body1>
            </SquareButton>
          </div>

          <NameList list={selectedGame.alternativeNames} erasable />

          <div className='game-submit'>
            <SquareButton size='block'>
              <Body1>등록 및 수정</Body1>
            </SquareButton>
          </div>
        </form>
      </div>
    </section>
  );
};

export default GameManagement;
