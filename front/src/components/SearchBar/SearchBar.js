import './SearchBar.scss';

import React, { useEffect, useRef, useState } from 'react';

import Caption1 from '../../core/Typography/Caption1';
import { FiSearch } from 'react-icons/fi';
import PropTypes from 'prop-types';
import getKorRegExp from './service/getKorRegExp';

const SearchBar = ({
  placeholder = '태그를 검색해주세요!',
  autoCompleteKeywords,
}) => {
  const [autoCompleteList, setAutoCompleteList] = useState([]);
  const autoCompleteRef = useRef(null);

  const onFocusInput = (e) => {
    autoCompleteRef.current.classList.add('show');
  };

  const onBlurInput = (e) => {
    autoCompleteRef.current.classList.remove('show');
  };

  const onChangeInput = (e) => {
    const inputValue = e.target.value;

    const searchResults = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+/g.test(inputValue)
      ? autoCompleteKeywords.filter((autoCompleteKeyword) => {
          const keywordRegExp = getKorRegExp(inputValue, {
            initialSearch: true,
            fuzzy: true,
          });
          return autoCompleteKeyword.name.match(keywordRegExp);
        })
      : autoCompleteKeywords.filter((autoCompleteKeyword) => {
          const searchRegex = new RegExp(inputValue, 'gi');
          const keywordWithoutSpace = autoCompleteKeyword.name.replace(
            /\s/g,
            ''
          );
          return (
            keywordWithoutSpace.match(searchRegex) ||
            autoCompleteKeyword.name.match(searchRegex)
          );
        });
    setAutoCompleteList(searchResults);
  };

  useEffect(() => {
    setAutoCompleteList(autoCompleteKeywords);
  }, []);

  return (
    <div className='searchbar-container'>
      <FiSearch size='24px' />
      <input
        type='search'
        className='searchbar'
        placeholder={placeholder}
        onFocus={onFocusInput}
        onBlur={onBlurInput}
        onChange={onChangeInput}
      />
      <ul className='auto-complete-container' ref={autoCompleteRef}>
        {autoCompleteList.length ? (
          autoCompleteList.map((autoCompleteItem, index) => (
            <li key={index}>
              <button type='button' className='auto-complete-button'>
                {autoCompleteItem?.name}
              </button>
            </li>
          ))
        ) : (
          <li className='result-not-found'>
            <Caption1>결과를 찾을 수 없습니다.</Caption1>
          </li>
        )}
      </ul>
    </div>
  );
};

SearchBar.propTypes = {
  placeholder: PropTypes.string,
  autoCompleteKeywords: PropTypes.arrayOf(
    PropTypes.shape({ name: PropTypes.string })
  ),
};

export default SearchBar;
