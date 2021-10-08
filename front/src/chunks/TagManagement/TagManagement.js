import './TagManagement.scss';

import { Body1, Subtitle1, Subtitle3 } from '../../core/Typography';
import React, { useState } from 'react';
import { SquareButton, TextInput } from '../../components';

import Headline2 from '../../core/Typography/Headline2';
import NameList from '../../components/NameList/NameList';
import PropTypes from 'prop-types';

const TagManagement = ({ tags = [] }) => {
  const [tagList, setTagList] = useState(tags);
  const [selectedTag, setSelectedTag] = useState({
    id: -1,
    name: '',
    alternativeNames: [],
  });
  const [alternativeNamesToRegister, setAlternativeNamesToRegister] = useState(
    []
  );

  return (
    <section className='tag-management-container'>
      <Headline2>태그 관리</Headline2>
      <div className='tag-management-wrapper'>
        <Subtitle1>태그 조회</Subtitle1>
        <NameList list={tagList} erasable />

        <Subtitle1>대체 이름 조회</Subtitle1>
        <NameList list={selectedTag.alternativeNames} erasable />

        <Subtitle1>태그 등록</Subtitle1>
        <form className='register-tag'>
          <Subtitle3>태그 이름</Subtitle3>
          <TextInput placeholder='태그 이름' />

          <Subtitle3>대체 이름(복수 개 가능)</Subtitle3>
          <div className='register-alternative-tag-name'>
            <TextInput placeholder='태그 대체 이름' />
            <SquareButton size='block'>
              <Body1>등록</Body1>
            </SquareButton>
          </div>

          <NameList list={alternativeNamesToRegister} erasable />

          <div className='tag-submit'>
            <SquareButton size='block'>
              <Body1>등록 및 수정</Body1>
            </SquareButton>
          </div>
        </form>
      </div>
    </section>
  );
};

TagManagement.propTypes = {
  tags: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number,
      name: PropTypes.string,
      alternativeNames: PropTypes.arrayOf(PropTypes.string),
    })
  ),
};

export default TagManagement;
