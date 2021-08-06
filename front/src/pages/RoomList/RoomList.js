import './RoomList.scss';

import { Body2, Headline2 } from '../../core/Typography';
import { Link, useLocation } from 'react-router-dom';
import {
  MainImage,
  NicknameSection,
  Room,
  SearchInput,
  SquareButton,
} from '../../components';
import React, { useEffect, useState } from 'react';
import { getLocalStorage, setLocalStorage } from '../../utils/localStorage';

import ChattingRoom from '../ChattingRoom/ChattingRoom';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import PropTypes from 'prop-types';
import TagList from '../../chunks/TagList/TagList';
import axios from 'axios';
import getKorRegExp from '../../components/SearchInput/service/getKorRegExp';
import { getRandomNickname } from '@woowa-babble/random-nickname';
import { useChattingModal } from '../../contexts/ChattingModalProvider';
import useInterval from '../../hooks/useInterval';
import { useUser } from '../../contexts/UserProvider';

const RoomList = ({ match }) => {
  const location = useLocation();
  const [imageUrl, setImageUrl] = useState('');
  const [tagList, setTagList] = useState([]);
  const [selectedTagList, setSelectedTagList] = useState([]);
  const [roomList, setRoomList] = useState([]);
  const { user, changeUser } = useUser();
  const { openChatting, closeChatting } = useChattingModal();

  // TODO: 임시 방편. onChangeInput을 SearchInput 내부로 집어넣으면서 사라질 운명
  const [autoCompleteTagList, setAutoCompleteTagList] = useState([]);

  const { gameId } = match.params;
  const { gameName } = location.state;

  const getImage = async () => {
    const response = await axios.get(
      `https://babble-test.o-r.kr/api/games/${gameId}/images`
    );
    const image = response.data.image;

    setImageUrl(image);
  };

  const getTags = async () => {
    const response = await axios.get('https://babble-test.o-r.kr/api/tags');
    const tags = response.data;

    setTagList(tags);
    setAutoCompleteTagList(tags);
  };

  const getRooms = async (tagIds) => {
    const response = await axios.get('https://babble-test.o-r.kr/api/rooms', {
      params: { gameId, tagIds, page: 1 },
    });
    const rooms = response.data;

    setRoomList(rooms);
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
        `https://babble-test.o-r.kr/api/rooms/${selectedRoomId}`
      );

      const { tags, roomId, createdDate } = response.data;

      closeChatting();
      openChatting(
        <ChattingRoom tags={tags} roomId={roomId} createdAt={createdDate} />
      );
    } catch (error) {
      alert('방이 존재하지 않습니다.');
      console.error(error);
    }
  };

  const onChangeTagInput = (e) => {
    const inputValue = e.target.value;

    const searchResults = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+/g.test(inputValue)
      ? tagList.filter((tag) => {
          const keywordRegExp = getKorRegExp(inputValue, {
            initialSearch: true,
            ignoreSpace: true,
          });
          return tag.name.match(keywordRegExp);
        })
      : tagList.filter((tag) => {
          const searchRegex = new RegExp(inputValue, 'gi');
          const keywordWithoutSpace = tag.name.replace(/\s/g, '');
          return (
            keywordWithoutSpace.match(searchRegex) ||
            tag.name.match(searchRegex)
          );
        });

    setAutoCompleteTagList(searchResults);
  };

  const getUserId = async () => {
    const newUser = { id: -1, nickname: '' };
    newUser.nickname = getLocalStorage('nickname');

    if (!newUser.nickname) {
      newUser.nickname = `${getRandomNickname('characters')}`;
      setLocalStorage('nickname', newUser.nickname);
    }

    const response = await axios.post('https://babble-test.o-r.kr/api/users', {
      nickname: newUser.nickname,
    });
    newUser.id = response.data.id;
    newUser.nickname = response.data.nickname;
    changeUser(newUser);
  };

  useEffect(() => {
    getImage();
    getRooms('');
    getTags();

    if (user.id === -1) {
      getUserId();
    }
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
      <MainImage imageSrc={imageUrl} />
      <PageLayout>
        <section className='room-list-header'>
          <Headline2>{gameName}</Headline2>
          <div className='side'>
            <NicknameSection />
            <Link
              to={{
                pathname: `${PATH.ROOM_LIST}/${gameId}${PATH.MAKE_ROOM}`,
                state: {
                  gameName,
                },
              }}
            >
              <SquareButton size='medium' colored>
                <Body2>방 생성하기</Body2>
              </SquareButton>
            </Link>
          </div>
        </section>

        <section className='search-section'>
          <SearchInput
            autoCompleteKeywords={autoCompleteTagList}
            onClickKeyword={selectTag}
            onChangeInput={onChangeTagInput}
          />
          <TagList tags={selectedTagList} onDeleteTag={eraseTag} erasable />
        </section>

        <section className='room-list-section'>
          {roomList.map((room, index) => (
            <Room room={room} key={index} onClickRoom={joinChatting} />
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
