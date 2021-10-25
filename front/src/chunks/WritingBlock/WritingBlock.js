import './WritingBlock.scss';
import '../../../node_modules/quill/dist/quill.snow.css';

import React, { useEffect, useRef } from 'react';

import PropTypes from 'prop-types';
import Quill from 'quill';
import { TextInput } from '../../components';

const WritingBlock = () => {
  const editorRef = useRef(null);

  useEffect(() => {
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

  return (
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
  );
};

export default WritingBlock;
