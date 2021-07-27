import './SearchInput.scss';

import React, { useRef } from 'react';

import PropTypes from 'prop-types';

const TextInput = ({ placeholder = '닉네임을 입력해주세요.' }) => {
  const containerRef = useRef(null);

  const onFocusInput = (e) => {
    containerRef.current.classList.add('focused');
  };

  const onBlurInput = (e) => {
    containerRef.current.classList.remove('focused');
  };

  return (
    <div
      className='input-container'
      onFocus={onFocusInput}
      onBlur={onBlurInput}
      ref={containerRef}
    >
      <input type='text' className='input-inner' placeholder={placeholder} />
    </div>
  );
};

TextInput.propTypes = {
  placeholder: PropTypes.string,
};

export default TextInput;
