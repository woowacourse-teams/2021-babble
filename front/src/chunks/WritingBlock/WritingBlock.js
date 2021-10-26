import './WritingBlock.scss';
import '../../../node_modules/quill/dist/quill.snow.css';

import React, { useEffect, useRef } from 'react';

import PropTypes from 'prop-types';
import Quill from 'quill';
import { TextInput } from '../../components';
import useUpdateEffect from '../../hooks/useUpdateEffect';

// import ReactHtmlParser from 'react-html-parser';

const WritingBlock = ({ title, content, nickname }) => {
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

  useUpdateEffect(() => {
    const blank = editorRef.current.root.querySelector('p');
    editorRef.current.root.removeChild(blank);

    const parser = new DOMParser();
    const contentString = parser.parseFromString(content, 'text/html');
    const paragraphs = contentString.querySelectorAll('p');

    paragraphs.forEach((p) => {
      editorRef.current.root.appendChild(p);
    });
  }, [editorRef.current]);

  return (
    <div className='writing-block'>
      <div className='title'>
        <TextInput
          name='title'
          defaultValue={title ?? ''}
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
          defaultValue={nickname ?? ''}
          border={false}
          placeholder='닉네임'
          disabled={nickname ? true : false}
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

WritingBlock.propTypes = {
  title: PropTypes.string,
  content: PropTypes.string,
  nickname: PropTypes.string,
};

export default WritingBlock;
