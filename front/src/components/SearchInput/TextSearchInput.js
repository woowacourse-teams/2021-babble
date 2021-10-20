import { FiSearch } from '@react-icons/all-files/fi/FiSearch';
import PropTypes from 'prop-types';
import React from 'react';

const TextSearchInput = ({ placeholder = '키워드를 입력해주세요.' }) => {
  return (
    <div className='input-container'>
      <input
        type='search'
        className='input-inner text-search'
        placeholder={placeholder}
      />
      <FiSearch size='20px' />
    </div>
  );
};

TextSearchInput.propTypes = {
  placeholder: PropTypes.string,
};

export default TextSearchInput;
