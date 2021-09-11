import './SearchInput.scss';

import React, { useEffect, useRef, useState } from 'react';

import { Caption1 } from '../../core/Typography';
import { FiSearch } from '@react-icons/all-files/fi/FiSearch';
import PropTypes from 'prop-types';
import useDebounce from '../../hooks/useDebounce';

const SearchInput = ({
  placeholder = '태그를 검색해주세요.',
  autoCompleteKeywords,
  onClickKeyword,
  onChangeInput,
  isResetable = true,
}) => {
  const { debounce } = useDebounce();
  const [autoCompleteList, setAutoCompleteList] = useState([]);
  const containerRef = useRef(null);
  const autoCompleteRef = useRef(null);
  const inputRef = useRef(null);

  const onFocusInput = () => {
    containerRef.current.classList.add('focused');
    autoCompleteRef.current.classList.add('show');
  };

  const onBlurInput = () => {
    containerRef.current.classList.remove('focused');
    autoCompleteRef.current.classList.remove('show');
  };

  const onSelectItem = ({ target }) => {
    const selectedItem = target.textContent;
    onClickKeyword(selectedItem);

    onBlurInput();

    if (isResetable) {
      inputRef.current.value = '';
    } else {
      inputRef.current.value = selectedItem;
    }

    setAutoCompleteList(autoCompleteKeywords);
  };

  // TODO: GameList와 RoomList에 있는 자동완성 로직 하나로 합쳐서 SearchInput에 있어야 할 듯.

  const onChangeInputDebounced = (e) => {
    debounce(() => onChangeInput(e), 500);
  };

  useEffect(() => {
    setAutoCompleteList(autoCompleteKeywords);
  }, [autoCompleteKeywords]);

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
        ref={inputRef}
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
  onClickKeyword: PropTypes.func,
  onChangeInput: PropTypes.func,
  isResetable: PropTypes.bool,
};

export default SearchInput;
