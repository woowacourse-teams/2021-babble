import './SearchInput.scss';

import React, { useEffect, useRef } from 'react';

import PropTypes from 'prop-types';

const TextInput = ({
  value,
  name,
  isContentSelected = true,
  placeholder = '닉네임을 입력해주세요.',
}) => {
  const containerRef = useRef(null);

  const onFocusInput = () => {
    containerRef.current.classList.add('focused');
  };

  const onBlurInput = () => {
    containerRef.current.classList.remove('focused');
  };

  useEffect(() => {
    if (isContentSelected) {
      containerRef.current.querySelector('.input-inner').select();
    }
  }, []);

  return (
    <div className='input-container' ref={containerRef}>
      <input
        type='text'
        className='input-inner'
        name={name}
        placeholder={placeholder}
        onFocus={onFocusInput}
        onBlur={onBlurInput}
        defaultValue={value}
      />
    </div>
  );
};

TextInput.propTypes = {
  value: PropTypes.string,
  name: PropTypes.string.isRequired,
  isContentSelected: PropTypes.bool,
  placeholder: PropTypes.string,
};

export default TextInput;
