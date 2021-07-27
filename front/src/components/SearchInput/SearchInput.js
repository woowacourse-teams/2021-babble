import './SearchInput.scss';

import React, { useEffect, useRef, useState } from 'react';

import Caption1 from '../../core/Typography/Caption1';
import { FiSearch } from 'react-icons/fi';
import PropTypes from 'prop-types';
import getKorRegExp from './service/getKorRegExp';
import useDebounce from '../../hooks/useDebounce';

const SearchInput = ({
  placeholder = '태그를 검색해주세요.',
  autoCompleteKeywords,
}) => {
  const { debounce } = useDebounce(500);
  const [autoCompleteList, setAutoCompleteList] = useState([]);
  const containerRef = useRef(null);
  const autoCompleteRef = useRef(null);

  const onFocusInput = (e) => {
    containerRef.current.classList.add('focused');
    autoCompleteRef.current.classList.add('show');
  };

  const onBlurInput = (e) => {
    containerRef.current.classList.remove('focused');
    autoCompleteRef.current.classList.remove('show');
  };

  const onSelectItem = (e) => {
    containerRef.current.classList.remove('focused');
    autoCompleteRef.current.classList.remove('show');
  };

  const onChangeInput = (e) => {
    const inputValue = e.target.value;

    const searchResults = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+/g.test(inputValue)
      ? autoCompleteKeywords.filter((autoCompleteKeyword) => {
          const keywordRegExp = getKorRegExp(inputValue, {
            initialSearch: true,
            ignoreSpace: true,
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

  const onChangeInputDebounced = (e) => {
    debounce(() => onChangeInput(e));
  };

  useEffect(() => {
    setAutoCompleteList(autoCompleteKeywords);
  }, []);

  return (
    <div className='input-container' ref={containerRef}>
      <FiSearch size='24px' />
      <input
        type='search'
        className='input-inner'
        placeholder={placeholder}
        onFocus={onFocusInput}
        onBlur={onBlurInput}
        onChange={onChangeInputDebounced}
      />
      <ul className='keyword-list-container' ref={autoCompleteRef}>
        {autoCompleteList.length ? (
          autoCompleteList.map((autoCompleteItem, index) => (
            <li key={index}>
              <button
                type='button'
                className='keyword-button'
                onMouseDown={onSelectItem}
              >
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

SearchInput.propTypes = {
  placeholder: PropTypes.string,
  autoCompleteKeywords: PropTypes.arrayOf(
    PropTypes.shape({ name: PropTypes.string })
  ),
};

export default SearchInput;
