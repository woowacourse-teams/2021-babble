import './WritingBlock.scss';
import '../../../node_modules/quill/dist/quill.snow.css';

import { Body2, Subtitle1 } from '../../core/Typography';
import { DropdownInput, SquareButton, TextInput } from '../../components';
import { useEffect, useRef, useState } from 'react';

import Quill from 'quill';

const WritingBlock = () => {
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  const editorRef = useRef(null);

  useEffect(() => {
    // TODO: api 완성되면 더미데이터 제거
    setCategories([
      '자유',
      'League of Legends',
      'Apex Legends',
      'Kart Rider Plus',
      'Overwatch',
      'Hearthstone',
    ]);

    editorRef.current = new Quill('.editor', {
      modules: {
        toolbar: [
          [{ font: [] }],
          // [{ header: [1, 2, 3, false] }], // header는 줄바꿈하면 풀린다.
          [{ size: ['small', false, 'large', 'huge'] }], // size는 풀리지는 않는 대신, 줄바꿈 시 약간 불안정함.
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
    // 이미지 저장하는 다른 방법
    // https://velog.io/@holim0/React-Quill-%EC%82%AC%EC%9A%A9%ED%95%B4%EB%B3%B4%EA%B8%B0
    // https://velog.io/@sklsh917/React-Quill%EC%9D%84-%ED%99%9C%EC%9A%A9%ED%95%98%EC%97%AC-%EA%B2%8C%EC%8B%9C%ED%8C%90-%EB%A7%8C%EB%93%A4%EA%B8%B0with-TypeScript

    // form 제출 로직
    // https://quilljs.com/playground/#form-submit
  }, []);

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
              type='borderless'
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
              type='borderless'
              placeholder='닉네임을 입력하세요.'
              required
            />
            <TextInput
              name='password'
              minLength={4}
              type='borderless'
              placeholder='비밀번호를 입력하세요.'
              required
            />
          </div>
        </div>
        <SquareButton
          type='submit'
          size='block'
          name='write'
          onClickButton={() => {}}
        >
          <Body2>작성하기</Body2>
        </SquareButton>
      </form>
    </section>
  );
};

export default WritingBlock;
