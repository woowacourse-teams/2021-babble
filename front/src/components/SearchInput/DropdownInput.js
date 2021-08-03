import './SearchInput.scss';

import React, { useEffect, useRef, useState } from 'react';

import { Caption1 } from '../../core/Typography';
import { IoCaretDown } from 'react-icons/io5';
import PropTypes from 'prop-types';

const DropdownInput = ({
  placeholder = '방 인원을 선택해주세요.',
  dropdownKeywords,
  maxHeadCount,
  setMaxHeadCount,
}) => {
  const [dropdownList, setDropdownList] = useState([]);
  const containerRef = useRef(null);
  const dropdownRef = useRef(null);
  const inputRef = useRef(null);

  const onFocusInput = () => {
    containerRef.current.classList.add('focused');
    dropdownRef.current.classList.add('show');
  };

  const onBlurInput = () => {
    containerRef.current.classList.remove('focused');
    dropdownRef.current.classList.remove('show');
  };

  const onSelectItem = (e) => {
    const selectedValue = Number(e.target.textContent);
    setMaxHeadCount(selectedValue);

    containerRef.current.classList.remove('focused');
    dropdownRef.current.classList.remove('show');
  };

  useEffect(() => {
    setDropdownList(dropdownKeywords);
  }, []);

  return (
    <div className='input-container' ref={containerRef}>
      <input
        type='number'
        className='input-inner'
        value={maxHeadCount ? maxHeadCount : ''}
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
  maxHeadCount: PropTypes.number,
  setMaxHeadCount: PropTypes.func,
  dropdownKeywords: PropTypes.arrayOf(PropTypes.number),
};

export default DropdownInput;
