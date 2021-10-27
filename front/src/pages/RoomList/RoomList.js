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
import { IoMdRefresh } from '@react-icons/all-files/io/IoMdRefresh';
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
import useScript from '../../hooks/useScript';
import useUpdateEffect from '../../hooks/useUpdateEffect';
import { useUser } from '../../contexts/UserProvider';

const RoomList = ({ match }) => {
  const [tagList, setTagList] = useState([]);
  const [selectedTagList, setSelectedTagList] = useState([]);
  const [roomList, setRoomList] = useState([]);
  const [currentGame, setCurrentGame] = useState({});

  // TODO: 임시 방편. onChangeInput을 SearchInput 내부로 집어넣으면서 사라질 운명
  const [autoCompleteTagList, setAutoCompleteTagList] = useState([]);
  const pageRef = useRef(0);
  const searchRef = useRef(null);
  const lastRoomRef = useRef(null);
  const infiniteObserverRef = useRef(null);

  const { user, changeUser } = useUser();
  const { openChatting, closeChatting, isChattingModalOpen } =
    useChattingModal();
  const { openModal, onConfirm } = useDefaultModal();

  useScript('https://developers.kakao.com/sdk/js/kakao.js');
  const history = useHistory();

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
      openModal(<ModalError>{error.message}</ModalError>);
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
      openModal(<ModalError>{error.message}</ModalError>);
    }
  };

  const getTags = async () => {
    try {
      const response = await axios.get(`${BASE_URL}/api/tags`);
      const tags = response.data;

      setTagList(tags);
      setAutoCompleteTagList(tags);
    } catch (error) {
      openModal(<ModalError>{error.message}</ModalError>);
    }
  };

  const getRooms = async (tagIds) => {
    if (!pageRef.current) return;
    try {
      const response = await axios.get(`${BASE_URL}/api/rooms`, {
        params: { gameId, tagIds, page: pageRef.current },
      });
      const rooms = response.data;

      if (!rooms.length) {
        infiniteObserverRef.current && infiniteObserverRef.current.disconnect();
        return;
      }

      setRoomList((prevRooms) => [...prevRooms, ...rooms]);
    } catch (error) {
      openModal(<ModalError>{error.message}</ModalError>);
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
      openModal(<ModalError>방에 입장할 수 없습니다.</ModalError>);
      console.error(error);
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
      openModal(<ModalError>{error.message}</ModalError>);
    }
  };

  const getRoomsWithTag = () => {
    const selectedTagIdParam = selectedTagList.map(({ id }) => id).join(',');
    getRooms(selectedTagIdParam);
  };

  const onConfirmExit = (callback, condition) =>
    onConfirm(callback, condition, '입장중인 방에서 퇴장하시겠습니까?');

  const refresh = () => {
    setRoomList([]);
    pageRef.current = 1;
    getRoomsWithTag();
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

    pageRef.current = 1;
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

  useUpdateEffect(() => {
    infiniteObserverRef.current && infiniteObserverRef.current.disconnect();

    if (lastRoomRef.current) {
      infiniteObserverRef.current = new IntersectionObserver(
        ([entry]) => {
          if (entry.isIntersecting) {
            pageRef.current += 1;
            getRoomsWithTag();
          }
        },
        { threshold: 1 }
      );
      infiniteObserverRef.current.observe(lastRoomRef.current);
    }
  }, [roomList.length]);

  useUpdateEffect(() => {
    refresh();
  }, [selectedTagList]);

  return (
    <div className='room-list-container'>
      <MainImage imageSrc={currentGame.images?.[0]} />
      <PageLayout>
        <section className='room-list-header'>
          <Headline2>{currentGame.name}</Headline2>
          <div className='side'>
            <NicknameSection />
            <SquareButton
              size='medium'
              onClickButton={() =>
                onConfirmExit(() => enterMakeRoomPage(), isChattingModalOpen)
              }
              colored
            >
              <Body2>방 만들기</Body2>
            </SquareButton>
          </div>
        </section>
        <div className='search-container' ref={searchRef}>
          <section className='search-section'>
            <div className='search-refresh'>
              <SearchInput
                autoCompleteKeywords={autoCompleteTagList}
                onClickKeyword={selectTag}
                onChangeInput={onChangeTagInput}
              />
              <SquareButton onClickButton={refresh}>
                <IoMdRefresh size='24px' color='#fff' />
              </SquareButton>
            </div>
            <TagList tags={selectedTagList} onDeleteTag={eraseTag} erasable />
          </section>
        </div>
        <section className='room-list-section'>
          {roomList.length ? (
            roomList?.map((room, index) => {
              const isLastIndex = index === roomList.length - 1;
              return (
                <Room
                  key={index}
                  room={room}
                  onClickRoom={(e) =>
                    onConfirmExit(() => joinChatting(e), isChattingModalOpen)
                  }
                  scrollRef={isLastIndex ? lastRoomRef : null}
                />
              );
            })
          ) : (
            <div className='no-room'>
              <Body2>방이 존재하지 않습니다.</Body2>
            </div>
          )}
        </section>
      </PageLayout>
    </div>
  );
};

RoomList.propTypes = {
  match: PropTypes.object,
};

export default RoomList;
