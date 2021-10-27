import './WritingBlock.scss';
import '../../../node_modules/quill/dist/quill.snow.css';

import React, { useEffect, useRef } from 'react';

import PropTypes from 'prop-types';
import Quill from 'quill';
import { ModalError, TextInput } from '../../components';
import useUpdateEffect from '../../hooks/useUpdateEffect';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';
import { getShortNumberId } from '../../utils/id';
import { BABBLE_URL, BASE_URL } from '../../constants/api';
import axios from 'axios';

const WritingBlock = ({ title, content, nickname }) => {
  const editorRef = useRef(null);
  const fileInputRef = useRef(null);

  const { openModal } = useDefaultModal();

  const imageHandler = () => {
    const imageFile = fileInputRef.current.files[0];
    const imageReader = new FileReader();

    imageReader.onload = async () => {
      const data = new FormData();
      data.append('fileName', `img/board/${getShortNumberId()}.jpg`);
      data.append('file', imageFile);

      try {
        const imageResponse = await axios.post(`${BASE_URL}/api/images`, data);
        const imageURL = `${BABBLE_URL}/${imageResponse.data[1]}`;

        const image = document.createElement('img');
        image.setAttribute('src', imageURL);
        image.setAttribute('style', 'width: 50rem;');

        editorRef.current.root.appendChild(image);
        fileInputRef.current.value = null;
      } catch (error) {
        openModal(<ModalError>{error.response?.data?.message}</ModalError>);
      }
    };

    imageReader.onerror = () => {
      openModal(<ModalError>{imageReader.error}</ModalError>);
    };

    imageReader.readAsDataURL(imageFile);
  };

  const clickImageButton = () => {
    fileInputRef.current.click();
  };

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

    const toolbar = editorRef.current.getModule('toolbar');
    console.log(editorRef.current.getModule('toolbar'));
    toolbar.addHandler('image', clickImageButton);
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
          type='password'
          name='password'
          minLength={4}
          border={false}
          placeholder='비밀번호(4자 이상)'
          required
        />
      </div>
      <input type='file' hidden onChange={imageHandler} ref={fileInputRef} />
    </div>
  );
};

WritingBlock.propTypes = {
  title: PropTypes.string,
  content: PropTypes.string,
  nickname: PropTypes.string,
};

export default WritingBlock;
