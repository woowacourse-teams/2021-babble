import './MakeRoom.scss';

import React, { useEffect } from 'react';

import Body2 from '../../core/Typography/Body2';
import DropdownInput from '../../components/SearchInput/DropdownInput';
import Headline2 from '../../core/Typography/Headline2';
import { Link } from 'react-router-dom';
import MainImage from '../../components/MainImage/MainImage';
import PATH from '../../constants/path';
import PageLayout from '../../core/Layout/PageLayout';
import PropTypes from 'prop-types';
import RoundButton from '../../components/Button/RoundButton';
import SearchInput from '../../components/SearchInput/SearchInput';
import TagList from '../../chunks/TagList/TagList';
import axios from 'axios';

const MakeRoom = ({ gameId }) => {
  const [imageUrl, setImageUrl] = useState('');

  const getImage = async () => {
    const mainImage = await axios.get(
      `https://babble.o-r.kr/api/games/${gameId}/images`
    );

    setImageUrl(mainImage);
  };

  useEffect(() => {
    getImage();
  }, []);

  return (
    <main className='make-room-container'>
      <MainImage imageSrc={imageUrl} />
      <PageLayout type='narrow'>
        <Headline2>방 생성하기</Headline2>
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
            erasable
          />
        </section>
        <section className='buttons'>
          <Link to={PATH.HOME}>
            <RoundButton size='small'>
              <Body2>취소하기</Body2>
            </RoundButton>
          </Link>
          <Link to={PATH.HOME}>
            <RoundButton colored={true} size='small' onClick={() => {}}>
              <Body2>생성하기</Body2>
            </RoundButton>
          </Link>
        </section>
      </PageLayout>
    </main>
  );
};

MakeRoom.propTypes = {
  gameId: PropTypes.number,
};

export default MakeRoom;
