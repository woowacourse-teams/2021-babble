import './SearchInput.scss';

import React, { useEffect, useRef, useState } from 'react';

import Caption1 from '../../core/Typography/Caption1';
import { IoCaretDown } from 'react-icons/io5';
import PropTypes from 'prop-types';

const DropdownInput = ({
  placeholder = '방 인원을 선택해주세요.',
  dropdownKeywords,
}) => {
  const [dropdownList, setDropdownList] = useState([]);
  const containerRef = useRef(null);
  const dropdownRef = useRef(null);
  const inputRef = useRef(null);

  const onFocusInput = (e) => {
    containerRef.current.classList.add('focused');
    dropdownRef.current.classList.add('show');
  };

  const onBlurInput = (e) => {
    containerRef.current.classList.remove('focused');
    dropdownRef.current.classList.remove('show');
  };

  const onSelectItem = (e) => {
    const selectedValue = e.target.textContent;
    inputRef.current.value = selectedValue;
    containerRef.current.classList.remove('focused');
    dropdownRef.current.classList.remove('show');
  };

  const onClickArrowDown = (e) => {
    containerRef.current.classList.toggle('focused');
    dropdownRef.current.classList.toggle('show');
  };

  useEffect(() => {
    setDropdownList(dropdownKeywords);
  }, []);

  return (
    <div className='input-container' ref={containerRef}>
      <input
        type='number'
        className='input-inner'
        placeholder={placeholder}
        onFocus={onFocusInput}
        onBlur={onBlurInput}
        ref={inputRef}
        readOnly
      />
      <IoCaretDown size='20px' onClick={onClickArrowDown} />
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
  dropdownKeywords: PropTypes.arrayOf(PropTypes.number),
};

export default DropdownInput;
