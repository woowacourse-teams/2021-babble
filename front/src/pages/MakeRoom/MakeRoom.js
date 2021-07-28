import './MakeRoom.scss';

import DropdownInput from '../../components/SearchInput/DropdownInput';
import MainImage from '../../components/MainImage/MainImage';
import PageLayout from '../../core/Layout/PageLayout';
import React from 'react';
import RoundButton from '../../components/Button/RoundButton';
import SearchInput from '../../components/SearchInput/SearchInput';
import Subtitle1 from '../../core/Typography/Subtitle1';
import TagList from '../../chunks/TagList/TagList';

// import PropTypes from 'prop-types';

const MakeRoom = () => {
  return (
    <main className='make-room-container'>
      <MainImage />
      <PageLayout type='narrow'>
        <Subtitle1>방 생성하기</Subtitle1>
        <section className='inputs'>
          <DropdownInput dropdownKeywords={[]} />
          <SearchInput autoCompleteKeywords={[]} />
        </section>
        <section className='tag-list-section'>
          <TagList
            tags={[
              { name: '실버' },
              { name: '골드 승급전' },
              { name: '원딜' },
              { name: '솔로랭크' },
              { name: '음성채팅 가능' },
            ]}
            tagType='erasable'
          />
        </section>
        <section className='buttons'>
          <RoundButton size='small'>취소하기</RoundButton>
          <RoundButton colored={true} size='small'>
            생성하기
          </RoundButton>
        </section>
      </PageLayout>
    </main>
  );
};

MakeRoom.propTypes = {};

export default MakeRoom;
