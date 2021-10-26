import { FiSearch } from '@react-icons/all-files/fi/FiSearch';
import PropTypes from 'prop-types';
import React from 'react';

const TextSearchInput = ({
  placeholder = '키워드를 입력해주세요.',
  onSearchButtonClick = () => {},
}) => {
  return (
    <div className='input-container'>
      <input
        type='search'
        name='search'
        className='input-inner text-search'
        placeholder={placeholder}
      />
      <button type='submit' onClick={onSearchButtonClick}>
        <FiSearch size='20px' />
      </button>
    </div>
  );
};

TextSearchInput.propTypes = {
  placeholder: PropTypes.string,
  onSearchButtonClick: PropTypes.func,
};

export default TextSearchInput;
