import './SearchInput.scss';

import React, { useEffect, useRef } from 'react';

import PropTypes from 'prop-types';

const TextInput = ({
  name,
  maxLength = 50,
  minLength = 1,
  defaultValue = null,
  onChangeInput = () => {},
  isContentSelected = true,
  placeholder = '닉네임을 입력해주세요.',
  inputRef = null,
  required = false,
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
        maxLength={maxLength}
        minLength={minLength}
        placeholder={placeholder}
        onChange={onChangeInput}
        onFocus={onFocusInput}
        onBlur={onBlurInput}
        defaultValue={defaultValue}
        ref={inputRef}
        required={required}
      />
    </div>
  );
};

TextInput.propTypes = {
  defaultValue: PropTypes.string,
  name: PropTypes.string.isRequired,
  placeholder: PropTypes.string,
  maxLength: PropTypes.number,
  minLength: PropTypes.number,
  onChangeInput: PropTypes.func,
  isContentSelected: PropTypes.bool,
  inputRef: PropTypes.object,
  required: PropTypes.bool,
};

export default TextInput;
