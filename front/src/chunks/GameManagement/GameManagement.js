import './GameManagement.scss';

import { BASE_URL, TEST_URL } from '../../constants/api';
import { Body1, Headline2, Subtitle1, Subtitle3 } from '../../core/Typography';
import { ModalError, SquareButton, TextInput } from '../../components';
import React, { useEffect, useRef, useState } from 'react';

import ImagePreview from '../../components/ImagePreview/ImagePreview';
import ImageRegister from '../../components/ImageRegister/ImageRegister';
import { NICKNAME_MAX_LENGTH } from '../../constants/chat';
import NameList from '../../components/NameList/NameList';
import axios from 'axios';
import { getNumberId } from '../../utils/id';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const GameManagement = () => {
  const gameNameRef = useRef(null);
  const alternativeNameRef = useRef(null);

  const [gameDetail, setGameDetail] = useState({
    id: -1,
    alternativeNames: [],
    images: [
      {
        name: '',
        imagePath: '',
      },
      {
        name: '',
        imagePath: '',
      },
      {
        name: '',
        imagePath: '',
      },
    ],
  });
  const [gameImageToSend, setGameImageToSend] = useState('');

  const [gameList, setGameList] = useState([]);
  const [isEditing, setIsEditing] = useState(false);
  const [isImageEditing, setIsImageEditing] = useState(false);

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

  const onSelectGame = (gameId) => {
    const selectedGameDetails = gameList.find((game) => game.id === gameId);

    gameNameRef.current.value = selectedGameDetails.name;

    setGameDetail({
      id: selectedGameDetails.id,
      alternativeNames: selectedGameDetails.alternativeNames,
      // TODO: 나중에 API 바뀌면 아래로 변경
      // images: selectedGameDetails.images.map((imagePath) => ({
      //   name: `${selectedGameDetails.name}.png`,
      //   imagePath,
      // })),
      images: [
        {
          name: `${selectedGameDetails.name}.png`,
          imagePath: selectedGameDetails.thumbnail,
        },
        {
          name: `${selectedGameDetails.name}.png`,
          imagePath: selectedGameDetails.thumbnail,
        },
        {
          name: `${selectedGameDetails.name}.png`,
          imagePath: selectedGameDetails.thumbnail,
        },
      ],
    });

    setIsEditing(true);
  };

  const onDeleteGame = async (gameId) => {
    // TODO: Custom modal로 바꾸는 작업 필요
    if (confirm('정말 삭제하시겠습니까?')) {
      try {
        await axios.delete(`${TEST_URL}/api/games/${gameId}`);
      } catch (error) {
        openModal(<ModalError>{error}</ModalError>);
      }
    }
  };

  const onSubmitForm = async (e) => {
    e.preventDefault();
    // post 이미지
    // gameDetail.imagePath
    if (isImageEditing) {
      try {
        const imageResponse = await axios.post(
          `${TEST_URL}/api/images`,
          // TODO: 데이터 형식이 base 64라는 상상
          gameImageToSend,
          {
            headers: {
              'content-type': 'multipart/form-data',
            },
          }
        );

        await axios.post(`${TEST_URL}/api/games`, {
          name: gameNameRef.current,
          images: imageResponse.data,
          alternativeNames: gameDetail.alternativeNames,
        });

        console.log(gameImageToSend);
        console.log(imageResponse);
      } catch (error) {
        openModal(<ModalError>{error}</ModalError>);
      }

      return;
    }

    await axios.post(`${TEST_URL}/api/games`, {
      name: gameNameRef.current,
      images: gameDetail.images,
      alternativeNames: gameDetail.alternativeNames,
    });
  };

  const onResetForm = (e) => {
    e.preventDefault();
    gameNameRef.current.value = '';
    alternativeNameRef.current.value = '';

    setGameDetail({
      id: -1,
      alternativeNames: [],
      images: [
        {
          name: '',
          imagePath: '',
        },
        {
          name: '',
          imagePath: '',
        },
        {
          name: '',
          imagePath: '',
        },
      ],
    });
    setGameImageToSend('');
    setIsEditing(false);
  };

  const setGameImage = (filename, imageFile) => {
    setGameDetail((detail) => ({
      ...detail,
      images: [
        { name: filename, imagePath: imageFile },
        { name: filename, imagePath: imageFile },
        { name: filename, imagePath: imageFile },
      ],
    }));
    setIsImageEditing(true);
  };

  const registerAlternativeName = ({ currentTarget }) => {
    const altName = currentTarget.form['alternative-name'].value.trim();
    if (!altName) {
      alternativeNameRef.current.value = '';
      alert('내용을 입력하세요!');
      return;
    }

    const duplicatedAltNameIndex = gameDetail.alternativeNames.findIndex(
      (currentName) => currentName.name === altName
    );
    if (duplicatedAltNameIndex !== -1) {
      alternativeNameRef.current.value = '';
      alert('중복된 이름입니다!');
      return;
    }

    const id = getNumberId();
    setGameDetail((detail) => ({
      ...detail,
      alternativeNames: [...detail.alternativeNames, { id, name: altName }],
    }));
    alternativeNameRef.current.value = '';
    alternativeNameRef.current.focus();
  };

  const deleteAlternativeName = (altId) => {
    if (!altId) return;

    const targetAltName = gameDetail.alternativeNames.filter(
      (altName) => altName.id !== altId
    );
    setGameDetail((details) => ({
      ...details,
      alternativeNames: targetAltName,
    }));
  };

  useEffect(() => {
    getGameList();
  }, []);

  return (
    <section className='game-management-container'>
      <Headline2>게임 관리</Headline2>
      <div className='game-management-wrapper'>
        <Subtitle1>게임 조회</Subtitle1>
        <NameList
          name='game-name'
          list={gameList}
          onClickName={onSelectGame}
          onDeleteName={onDeleteGame}
          erasable
        />

        <Subtitle1>게임 {isEditing ? `수정` : '등록'}</Subtitle1>

        <form className='register-edit-game' onSubmit={onSubmitForm}>
          <Subtitle3>게임 이름</Subtitle3>
          <div className='game-name-input'>
            <TextInput
              name='game-name'
              inputRef={gameNameRef}
              maxLength={NICKNAME_MAX_LENGTH}
              placeholder='게임 이름'
            />
          </div>
          <SquareButton type='reset' size='small' onClickButton={onResetForm}>
            <Body1>초기화</Body1>
          </SquareButton>
          <ImageRegister
            file={gameDetail.images[0]}
            setFile={setGameImage}
            setRegisterFile={setGameImageToSend}
          />
          <ImagePreview imageList={gameDetail.images} />

          <Subtitle3>대체 이름(복수 개 가능)</Subtitle3>

          <div className='game-alternative-input'>
            <TextInput
              name='alternative-name'
              maxLength={NICKNAME_MAX_LENGTH}
              inputRef={alternativeNameRef}
              placeholder='대체 이름'
            />
            <SquareButton size='block' onClickButton={registerAlternativeName}>
              <Body1>등록</Body1>
            </SquareButton>
          </div>
          <NameList
            list={gameDetail.alternativeNames}
            onDeleteName={deleteAlternativeName}
            erasable
          />

          <div className='game-submit'>
            <SquareButton size='block' type='submit'>
              <Body1>등록 및 수정</Body1>
            </SquareButton>
          </div>
        </form>
      </div>
    </section>
  );
};

export default GameManagement;
