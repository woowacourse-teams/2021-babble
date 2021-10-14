import './GameManagement.scss';

import { BABBLE_URL, BASE_URL } from '../../constants/api';
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
      console.log(games);

      setGameList(games);
    } catch (error) {
      openModal(<ModalError>{error.message}</ModalError>);
    }
  };

  const onSelectGame = (gameId) => {
    const selectedGameDetails = gameList.find((game) => game.id === gameId);

    gameNameRef.current.value = selectedGameDetails.name;

    setGameDetail({
      id: selectedGameDetails.id,
      alternativeNames: selectedGameDetails.alternativeNames,
      images: selectedGameDetails.images.map((imagePath) => ({
        name: `${selectedGameDetails.name}.png`,
        imagePath: imagePath ?? '',
      })),
    });

    setIsEditing(true);
  };

  const onDeleteGame = async (gameId) => {
    // TODO: Custom modal로 바꾸는 작업 필요
    if (confirm('정말 삭제하시겠습니까?')) {
      try {
        await axios.delete(`${BASE_URL}/api/games/${gameId}`);
        onResetForm();
        getGameList();
      } catch (error) {
        openModal(<ModalError>{error.message}</ModalError>);
      }
    }
  };

  const onSubmitForm = async (e) => {
    e.preventDefault();

    const form = e.currentTarget;
    if (isEditing) {
      if (isImageEditing) {
        // 게임 수정, 이미지 수정
        try {
          const data = new FormData();
          data.append(
            'fileName',
            `img/games/title/${gameNameRef.current.value}.jpg`
          );

          if (!gameImageToSend) {
            alert('이미지를 등록해주세요!');
            return;
          }

          data.append('file', gameImageToSend);

          const imageResponse = await axios.post(
            `${BASE_URL}/api/images`,
            data
          );

          const fullURLResponse = imageResponse.data.map(
            (url) => `${BABBLE_URL}/${url}`
          );

          await axios.put(`${BASE_URL}/api/games/${gameDetail.id}`, {
            name: gameNameRef.current.value,
            images: fullURLResponse,
            alternativeNames: gameDetail.alternativeNames.map(
              (altName) => altName.name
            ),
          });

          alert('정상적으로 수정되었습니다!');
        } catch (error) {
          console.error(error);
          openModal(<ModalError>{error.message}</ModalError>);
        }

        getGameList();
        form.reset.click();
        return;
      }

      // 게임 수정, 이미지 수정 X
      try {
        if (!gameDetail.images[0].imagePath) {
          alert('이미지를 등록해주세요!');
          return;
        }

        await axios.put(`${BASE_URL}/api/games/${gameDetail.id}`, {
          name: gameNameRef.current.value,
          images: gameDetail.images.map((image) => image.imagePath),
          alternativeNames: gameDetail.alternativeNames.map(
            (altName) => altName.name
          ),
        });

        alert('정상적으로 수정되었습니다!');
      } catch (error) {
        openModal(<ModalError>{error.message}</ModalError>);
      }

      getGameList();
      form.reset.click();
      return;
    }

    // 게임 새로 등록
    try {
      const data = new FormData();
      data.append(
        'fileName',
        `img/games/title/${gameNameRef.current.value}.jpg`
      );

      if (!gameImageToSend) {
        alert('이미지를 등록해주세요!');
        return;
      }

      data.append('file', gameImageToSend);

      const imageResponse = await axios.post(`${BASE_URL}/api/images`, data);

      const fullURLResponse = imageResponse.data.map(
        (url) => `${BABBLE_URL}/${url}`
      );

      await axios.post(`${BASE_URL}/api/games`, {
        name: gameNameRef.current.value,
        images: fullURLResponse,
        alternativeNames: gameDetail.alternativeNames.map(
          (altName) => altName.name
        ),
      });

      alert('정상적으로 등록되었습니다!');
    } catch (error) {
      openModal(<ModalError>{error.message}</ModalError>);
    }

    getGameList();
    form.reset.click();
  };

  const onResetForm = (e) => {
    if (e) {
      e.preventDefault();
    }

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
    setIsImageEditing(false);
  };

  const setGameImageToShow = (filename, imageFile) => {
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

  const registerAlternativeName = (e) => {
    const altName = e.currentTarget.form['alternative-name'].value.trim();
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

  const preventEnterSubmit = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      return;
    }
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

        <form
          className='register-edit-game'
          onSubmit={onSubmitForm}
          onKeyDown={preventEnterSubmit}
        >
          <div className='register-edit-game-header'>
            <Subtitle1>게임 {isEditing ? `수정` : '등록'}</Subtitle1>
            <SquareButton
              type='reset'
              size='small'
              name='reset'
              onClickButton={onResetForm}
            >
              <Body1>초기화</Body1>
            </SquareButton>
          </div>

          <Subtitle3>게임 이름</Subtitle3>
          <div className='game-name-input'>
            <TextInput
              name='game-name'
              inputRef={gameNameRef}
              maxLength={NICKNAME_MAX_LENGTH}
              placeholder='게임 이름'
              required
            />
          </div>
          <ImageRegister
            file={gameDetail.images[0]}
            setPreviewURL={setGameImageToShow}
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
              // onKeyDownInput={preventEnterSubmit}
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
