import './SearchInput.scss';

import React, { useEffect, useRef, useState } from 'react';

import { Caption1 } from '../../core/Typography';
import { IoCaretDown } from '@react-icons/all-files/io5/IoCaretDown';
import PropTypes from 'prop-types';

const DropdownInput = ({
  placeholder = '방 인원을 선택해주세요.',
  dropdownKeywords,
  inputValue,
  setInputValue,
}) => {
  const [dropdownList, setDropdownList] = useState([]);
  const containerRef = useRef(null);
  const dropdownRef = useRef(null);
  const inputRef = useRef(null);

  const onFocusInput = () => {
    dropdownRef.current.classList.add('show');
  };

  const onBlurInput = () => {
    dropdownRef.current.classList.remove('show');
  };

  const onSelectItem = (e) => {
    const selectedValue = Number(e.target.textContent);
    setInputValue(selectedValue);

    dropdownRef.current.classList.remove('show');
  };

  useEffect(() => {
    setDropdownList(dropdownKeywords);
  }, []);

  return (
    <div className='input-container' ref={containerRef}>
      <input
        type='text'
        className='input-inner'
        value={inputValue ? inputValue : ''}
        placeholder={placeholder}
        onFocus={onFocusInput}
        onBlur={onBlurInput}
        ref={inputRef}
        readOnly
      />
      <IoCaretDown size='20px' />
      <ul className='keyword-list-container' ref={dropdownRef}>
        {dropdownList.length ? (
          dropdownList.map((dropdownItem, index) => (
            <li key={index}>
              <button
                type='button'
                className='keyword-button'
                onMouseDown={onSelectItem}
              >
                {dropdownItem}
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

DropdownInput.propTypes = {
  placeholder: PropTypes.string,
  inputValue: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  setInputValue: PropTypes.func,
  dropdownKeywords: PropTypes.arrayOf(
    PropTypes.oneOfType([PropTypes.number, PropTypes.string])
  ),
};

export default DropdownInput;
