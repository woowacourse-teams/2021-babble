import './TagManagement.scss';

import { Body1, Subtitle1, Subtitle3 } from '../../core/Typography';
import React, { useEffect, useRef, useState } from 'react';
import { SquareButton, TextInput } from '../../components';

import { BASE_URL } from '../../constants/api';
import Headline2 from '../../core/Typography/Headline2';
import { ModalError } from '../../components';
import { NICKNAME_MAX_LENGTH } from '../../constants/chat';
import NameList from '../../components/NameList/NameList';
import axios from 'axios';
import { getNumberId } from '../../utils/id';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const TagManagement = () => {
  const tagNameInputRef = useRef(null);
  const alternativeNameInputRef = useRef(null);

  const [tagList, setTagList] = useState([]);
  const [selectedTag, setSelectedTag] = useState({
    id: -1,
    name: '',
    alternativeNames: [],
  });
  const [alternativeNamesToRegister, setAlternativeNamesToRegister] = useState(
    []
  );

  const { openModal } = useDefaultModal();

  const getTags = async () => {
    try {
      const response = await axios.get(`${BASE_URL}/api/tags`);
      const tags = response.data;
      setTagList(tags);
    } catch (error) {
      openModal(<ModalError>{error.response?.data?.message}</ModalError>);
    }
  };

  const selectTag = (tagId) => {
    if (!tagId) return;

    const targetTag = tagList.find((tag) => tag.id === tagId);
    setSelectedTag(targetTag);
  };

  const deleteTag = async (tagId) => {
    if (!tagId) return;

    if (confirm('정말 삭제하시겠습니까?')) {
      try {
        await axios.delete(`${BASE_URL}/api/tags/${tagId}`);
        resetForm();
        getTags();
      } catch (error) {
        openModal(<ModalError>{error.response?.data?.message}</ModalError>);
      }
    }
  };

  const submitTag = async (e) => {
    e.preventDefault();
    if (!tagNameInputRef.current.value) return;

    try {
      const newTag = {
        alternativeNames: alternativeNamesToRegister.map(
          (altName) => altName.name
        ),
        name: tagNameInputRef.current.value,
      };
      await axios.post(`${BASE_URL}/api/tags`, newTag);

      resetForm();
      getTags();
    } catch (error) {
      openModal(<ModalError>{error.response?.data?.message}</ModalError>);
    }
  };

  const registerAlternativeName = () => {
    const name = alternativeNameInputRef.current.value.trim();

    if (!name) {
      alternativeNameInputRef.current.value = '';
      alert('내용을 입력하세요!');
      return;
    }

    const duplicatedAltNameIndex = alternativeNamesToRegister.findIndex(
      (currentName) => currentName.name === name
    );
    if (duplicatedAltNameIndex !== -1) {
      alternativeNameInputRef.current.value = '';
      alert('중복된 이름입니다!');
      return;
    }

    const id = getNumberId();
    setAlternativeNamesToRegister([
      ...alternativeNamesToRegister,
      { id, name },
    ]);
    alternativeNameInputRef.current.value = '';
    alternativeNameInputRef.current.focus();
  };

  const deleteAlternativeName = (altId) => {
    if (!altId) return;

    const targetAltName = alternativeNamesToRegister.filter(
      (altName) => altName.id !== altId
    );
    setAlternativeNamesToRegister(targetAltName);
  };

  const resetForm = () => {
    tagNameInputRef.current.value = '';
    alternativeNameInputRef.current.value = '';
    setSelectedTag({
      id: -1,
      name: '',
      alternativeNames: [],
    });
    setAlternativeNamesToRegister([]);
  };

  const preventEnterSubmit = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
    }
  };

  useEffect(() => {
    getTags();
  }, []);

  return (
    <section className='tag-management-container'>
      <Headline2>태그 관리</Headline2>
      <div className='tag-management-wrapper'>
        <div className='tag-view'>
          <Subtitle1>태그 조회</Subtitle1>
          <NameList
            list={tagList}
            onClickName={selectTag}
            onDeleteName={deleteTag}
            erasable
          />

          <Subtitle1>대체 이름 조회</Subtitle1>
          <NameList list={selectedTag.alternativeNames} erasable />
        </div>

        <Subtitle1>태그 등록</Subtitle1>
        <form
          className='register-tag'
          onSubmit={submitTag}
          onKeyDown={preventEnterSubmit}
        >
          <Subtitle3>태그 이름</Subtitle3>
          <TextInput
            name='tag-name'
            maxLength={NICKNAME_MAX_LENGTH}
            placeholder='태그 이름'
            inputRef={tagNameInputRef}
          />

          <Subtitle3>대체 이름(복수 개 가능)</Subtitle3>
          <div className='register-alternative-tag-name'>
            <TextInput
              name='tag-alternative-name'
              maxLength={NICKNAME_MAX_LENGTH}
              placeholder='태그 대체 이름'
              inputRef={alternativeNameInputRef}
            />
            <SquareButton size='block' onClickButton={registerAlternativeName}>
              <Body1>등록</Body1>
            </SquareButton>
          </div>

          <NameList
            list={alternativeNamesToRegister}
            onDeleteName={deleteAlternativeName}
            erasable
          />
          <div className='tag-submit'>
            <SquareButton size='block' type='submit'>
              <Body1>등록 및 수정</Body1>
            </SquareButton>
          </div>
        </form>
      </div>
    </section>
  );
};

export default TagManagement;
