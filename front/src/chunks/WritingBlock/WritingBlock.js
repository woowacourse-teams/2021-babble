import './WritingBlock.scss';
import '../../../node_modules/quill/dist/quill.snow.css';

import { Body2, Subtitle1 } from '../../core/Typography';
import { DropdownInput, SquareButton, TextInput } from '../../components';
import React, { useEffect, useRef, useState } from 'react';

import PropTypes from 'prop-types';
import Quill from 'quill';

const WritingBlock = ({ submitPost }) => {
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  const editorRef = useRef(null);

  useEffect(() => {
    setCategories(['자유', '건의', '게임', '공지']);

    editorRef.current = new Quill('.editor', {
      modules: {
        toolbar: [
          [{ font: [] }],
          [{ size: ['small', false, 'large', 'huge'] }],
          ['bold', 'italic', 'underline', 'strike'],
          [{ align: [] }],
          [{ color: [] }, { background: [] }],
          [{ indent: '-1' }, { indent: '+1' }],
          ['link', 'image', 'video'],
        ],
      },
      placeholder: '내용을 입력하세요.',
      theme: 'snow',
    });
  }, []);

  const handleSubmitButtonClick = (e) => {
    e.preventDefault();
    const form = e.currentTarget.form;
    const title = form.title.value;
    const content = form.querySelector('.ql-editor').innerHTML;
    const nickname = form.nickname.value;
    const password = form.password.value;

    const post = {
      title,
      content,
      nickname,
      password,
      category: selectedCategory || '자유',
    };

    submitPost(post);
  };

  return (
    <section className='writing-section'>
      <div className='writing-header'>
        <Subtitle1>게시글 작성</Subtitle1>
        <div className='category'>
          <DropdownInput
            type='text'
            placeholder='카테고리를 선택하세요.'
            dropdownKeywords={categories}
            inputValue={selectedCategory}
            setInputValue={setSelectedCategory}
          />
        </div>
      </div>
      <form className='writing-form'>
        <div className='writing-block'>
          <div className='title'>
            <TextInput
              name='title'
              border={false}
              placeholder='제목을 입력하세요.'
              required
            />
          </div>
          <div className='editor-container'>
            <div className='editor'></div>
          </div>
          <div className='writing-info'>
            <TextInput
              name='nickname'
              border={false}
              placeholder='닉네임'
              required
            />
            <TextInput
              name='password'
              minLength={4}
              border={false}
              placeholder='비밀번호(4자 이상)'
              required
            />
          </div>
        </div>
        <SquareButton
          type='submit'
          size='block'
          name='write'
          onClickButton={handleSubmitButtonClick}
        >
          <Body2>작성하기</Body2>
        </SquareButton>
      </form>
    </section>
  );
};

WritingBlock.propTypes = {
  submitPost: PropTypes.func,
};

export default WritingBlock;
