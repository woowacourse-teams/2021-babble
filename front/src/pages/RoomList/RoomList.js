import './RoomList.scss';

import { Body2, Headline2 } from '../../core/Typography';
import {
  MainImage,
  ModalError,
  NicknameSection,
  Room,
  SearchInput,
  SquareButton,
} from '../../components';
import React, { useEffect, useRef, useState } from 'react';
import { getSessionStorage, setSessionStorage } from '../../utils/storage';

import { BASE_URL } from '../../constants/api';
import ChattingRoom from '../ChattingRoom/ChattingRoom';
import ModalConfirm from '../../components/Modal/ModalConfirm';
import PATH from '../../constants/path';
import { PATTERNS } from '../../constants/regex';
import PageLayout from '../../core/Layout/PageLayout';
import PropTypes from 'prop-types';
import TagList from '../../chunks/TagList/TagList';
import axios from 'axios';
import getKorRegExp from '../../components/SearchInput/service/getKorRegExp';
import { getRandomNickname } from '@woowa-babble/random-nickname';
import { useChattingModal } from '../../contexts/ChattingModalProvider';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';
import { useHistory } from 'react-router-dom';
import useInterval from '../../hooks/useInterval';
import useScript from '../../hooks/useScript';
import { useUser } from '../../contexts/UserProvider';

const RoomList = ({ match }) => {
  useScript('https://developers.kakao.com/sdk/js/kakao.js');
  const [tagList, setTagList] = useState([]);
  const [selectedTagList, setSelectedTagList] = useState([]);
  const [roomList, setRoomList] = useState([]);
  const [currentGame, setCurrentGame] = useState({});
  const { user, changeUser } = useUser();
  const { openChatting, closeChatting, isChattingModalOpen } =
    useChattingModal();
  const { openModal } = useDefaultModal();

  const history = useHistory();

  // TODO: 임시 방편. onChangeInput을 SearchInput 내부로 집어넣으면서 사라질 운명
  const [autoCompleteTagList, setAutoCompleteTagList] = useState([]);
  const searchRef = useRef(null);

  const { gameId } = match.params;

  const getRoomDataWhenEnterWithLink = async () => {
    const { roomId } = match.params;
    const response = await axios.get(`${BASE_URL}/api/rooms/${roomId}`);
    const { game, tags } = response.data;

    try {
      const response = await axios.post(`${BASE_URL}/api/users`, {
        nickname: getRandomNickname(),
      });
      const generatedUser = response.data;

      setSessionStorage('nickname', generatedUser.nickname);

      changeUser({ id: generatedUser.id, nickname: generatedUser.nickname });
    } catch (error) {
      console.log(error);
    }

    if (roomId) {
      if (match.url.includes(`/chat/${roomId}`)) {
        history.push(`/games/${gameId}`);
        openChatting(
          <ChattingRoom game={game} tags={tags} roomId={Number(roomId)} />
        );
      }
    }
  };

  useEffect(() => {
    if (match.params.roomId) {
      getRoomDataWhenEnterWithLink();
    }
  }, []);

  const getGame = async () => {
    try {
      const response = await axios.get(`${BASE_URL}/api/games/${gameId}`);

      setCurrentGame(response.data);
    } catch (error) {
      openModal(<ModalError>{error}</ModalError>);
    }
  };

  const getTags = async () => {
    try {
      const response = await axios.get(`${BASE_URL}/api/tags`);
      const tags = response.data;

      setTagList(tags);
      setAutoCompleteTagList(tags);
    } catch (error) {
      openModal(<ModalError>{error}</ModalError>);
    }
  };

  const getRooms = async (tagIds) => {
    try {
      const response = await axios.get(`${BASE_URL}/api/rooms`, {
        params: { gameId, tagIds, page: 1 },
      });
      const rooms = response.data;

      setRoomList(rooms);
    } catch (error) {
      openModal(<ModalError>{error}</ModalError>);
    }
  };

  const selectTag = (tagName) => {
    const tagId = tagList.find((tag) => tag.name === tagName).id;
    const tag = { id: tagId, name: tagName };

    setSelectedTagList((prevTagList) =>
      prevTagList.find((prevTag) => prevTag.name === tag.name)
        ? prevTagList
        : [...prevTagList, tag]
    );
  };

  const eraseTag = (e) => {
    // TODO: selectedTag 찾는 로직 고민해보기(element 구조에 종속적임)
    const selectedTagName = e.target
      .closest('.tag-container')
      .querySelector('.caption1').textContent;

    setSelectedTagList((prevTagList) =>
      prevTagList.filter((tag) => tag.name !== selectedTagName)
    );
  };

  const joinChatting = async (e) => {
    const selectedRoomId = e.target.closest('.room-container').dataset.roomId;

    try {
      const response = await axios.get(
        `${BASE_URL}/api/rooms/${selectedRoomId}`
      );
      const { tags, game, roomId } = response.data;

      if (isChattingModalOpen) {
        closeChatting();
      }

      openChatting(<ChattingRoom game={game} tags={tags} roomId={roomId} />);
    } catch (error) {
      openModal(<ModalError>{error}</ModalError>);
    }
  };

  const enterMakeRoomPage = () => {
    if (isChattingModalOpen) {
      closeChatting();
    }

    history.push({
      pathname: `${PATH.ROOM_LIST}/${gameId}${PATH.MAKE_ROOM}`,
      state: {
        gameName: currentGame.name,
      },
    });
  };

  // TODO: Custom Confirm 로직을 개선할 방법에 대해 고려(우선순위 꽤 높음).
  const onConfirm = (callback) => {
    if (isChattingModalOpen) {
      openModal(
        <ModalConfirm confirmCallback={callback}>
          입장중인 방에서 퇴장하시겠습니까?
        </ModalConfirm>
      );

      return;
    }

    callback();
  };

  const onChangeTagInput = (e) => {
    const inputValue = e.target.value.replace(PATTERNS.SPECIAL_CHARACTERS, '');

    const searchResults = PATTERNS.KOREAN.test(inputValue)
      ? tagList.filter((tag) => {
          const searchRegex = getKorRegExp(inputValue, {
            initialSearch: true,
            ignoreSpace: true,
          });
          return tag.name.match(searchRegex);
        })
      : tagList.filter((tag) => {
          const searchRegex = new RegExp(inputValue, 'gi');
          const keywordWithoutSpace = tag.name.replace(/\s/g, '');
          return (
            keywordWithoutSpace.match(searchRegex) ||
            tag.name.match(searchRegex)
          );
        });

    // 대안 이름으로 검색된 게임
    const alternativeResults = PATTERNS.KOREAN.test(inputValue)
      ? tagList.filter((tag) => {
          const searchRegex = getKorRegExp(inputValue, {
            initialSearch: true,
            ignoreSpace: true,
          });

          return tag.alternativeNames.some((alternativeName) =>
            alternativeName.match(searchRegex)
          );
        })
      : tagList.filter((tag) => {
          const searchRegex = new RegExp(inputValue, 'gi');
          const alternativeNamesWithoutSpace = tag.alternativeNames.map(
            (alternativeName) => alternativeName.replace(PATTERNS.SPACE, '')
          );

          return (
            alternativeNamesWithoutSpace.some((alternativeName) =>
              alternativeName.match(searchRegex)
            ) ||
            tag.alternativeNames.some((alternativeName) =>
              alternativeName.match(searchRegex)
            )
          );
        });

    setAutoCompleteTagList([...searchResults, ...alternativeResults]);
  };

  const getUserId = async () => {
    const newUser = { id: -1, nickname: '' };
    newUser.nickname = getSessionStorage('nickname');

    if (!newUser.nickname) {
      newUser.nickname = `${getRandomNickname('characters')}`;
      setSessionStorage('nickname', newUser.nickname);
    }

    try {
      const response = await axios.post(`${BASE_URL}/api/users`, {
        nickname: newUser.nickname,
      });
      newUser.id = response.data.id;
      newUser.nickname = response.data.nickname;
      changeUser(newUser);
    } catch (error) {
      openModal(<ModalError>{error}</ModalError>);
    }
  };

  useEffect(() => {
    const stickyObserver = new IntersectionObserver(
      ([entry]) => {
        entry.target.classList.toggle(
          'stuck',
          entry.intersectionRatio < 1 && !entry.isIntersecting
        );
      },
      { threshold: 1 }
    );
    stickyObserver.observe(searchRef.current);

    getRooms('');
    getTags();
    getGame();

    if (user.id === -1) {
      getUserId();
    }

    return () => {
      stickyObserver && stickyObserver.disconnect();
    };
  }, []);

  // TODO: 데모데이 이후 삭제될 운명
  useInterval(() => {
    const selectedTagIdParam = selectedTagList.map(({ id }) => id).join(',');
    getRooms(selectedTagIdParam);
  }, 5000);

  useEffect(() => {
    const selectedTagIdParam = selectedTagList.map(({ id }) => id).join(',');
    getRooms(selectedTagIdParam);
  }, [selectedTagList]);

  return (
    <div className='room-list-container'>
      <MainImage imageSrc={currentGame.thumbnail} />
      <PageLayout>
        <section className='room-list-header'>
          <Headline2>{currentGame.name}</Headline2>
          <div className='side'>
            <NicknameSection />
            <SquareButton
              size='medium'
              onClick={() => onConfirm(() => enterMakeRoomPage())}
              colored
            >
              <Body2>방 생성하기</Body2>
            </SquareButton>
          </div>
        </section>
        <div className='search-container' ref={searchRef}>
          <section className='search-section'>
            <SearchInput
              autoCompleteKeywords={autoCompleteTagList}
              onClickKeyword={selectTag}
              onChangeInput={onChangeTagInput}
            />
            <TagList tags={selectedTagList} onDeleteTag={eraseTag} erasable />
          </section>
        </div>
        <section className='room-list-section'>
          {roomList?.map((room, index) => (
            <Room
              key={index}
              room={room}
              onClickRoom={(e) => onConfirm(() => joinChatting(e))}
            />
          ))}
        </section>
      </PageLayout>
    </div>
  );
};

RoomList.propTypes = {
  match: PropTypes.object,
};

export default RoomList;
