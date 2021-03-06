import './SearchInput.scss';

import React, { useEffect, useRef } from 'react';

import PropTypes from 'prop-types';

const TextInput = ({
  name,
  type = 'text',
  border = true,
  maxLength = 50,
  minLength = 1,
  defaultValue = null,
  onChangeInput = () => {},
  isContentSelected = true,
  placeholder = '닉네임을 입력해주세요.',
  inputRef = null,
  required = false,
  disabled = false,
  onKeyDownInput = () => {},
}) => {
  const containerRef = useRef(null);

  useEffect(() => {
    if (isContentSelected) {
      containerRef.current.querySelector('.input-inner').select();
    }
  }, []);

  return (
    <div
      className={`input-container ${border ? '' : 'borderless'}`}
      ref={containerRef}
    >
      <input
        type={type}
        className='input-inner'
        name={name}
        maxLength={maxLength}
        minLength={minLength}
        placeholder={placeholder}
        onChange={onChangeInput}
        defaultValue={defaultValue}
        ref={inputRef}
        disabled={disabled}
        onKeyDown={onKeyDownInput}
        required={required}
      />
    </div>
  );
};

TextInput.propTypes = {
  type: PropTypes.string,
  defaultValue: PropTypes.string,
  border: PropTypes.bool,
  name: PropTypes.string.isRequired,
  placeholder: PropTypes.string,
  maxLength: PropTypes.number,
  minLength: PropTypes.number,
  onChangeInput: PropTypes.func,
  isContentSelected: PropTypes.bool,
  inputRef: PropTypes.object,
  onKeyDownInput: PropTypes.func,
  disabled: PropTypes.bool,
  required: PropTypes.bool,
};

export default TextInput;
