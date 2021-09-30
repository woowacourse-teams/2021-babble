import './ImageRegister.scss';

import { Caption1, Subtitle3 } from '../../core/Typography';
import React, { useRef, useState } from 'react';

import { BsPlusCircle } from '@react-icons/all-files/bs/BsPlusCircle';

const ImageRegister = () => {
  const fileInputRef = useRef(null);
  const [file, setFile] = useState({ name: '', imagePath: '' });

  const handleFile = ({ target }) => {
    const file = target.files[0];
    const filename = file.name;

    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      setFile((prevData) => ({ ...prevData, imagePath: reader.result }));
    };

    setFile((prevData) => ({ ...prevData, name: filename }));
  };

  return (
    <section className='input-register-container'>
      <Subtitle3>이미지 등록</Subtitle3>
      <div className='input-drag-drop'>
        <div className='drag-drop-zone'>
          <input
            type='file'
            accept='image/*'
            className='file-dropdown'
            onChange={handleFile}
            ref={fileInputRef}
          />
          {file.imagePath === '' ? (
            <BsPlusCircle size='30' color='#8d8d8d' />
          ) : (
            <div className='input-image-preview'>
              <img src={file.imagePath} alt='preview-image' />
            </div>
          )}
        </div>
        <Caption1>{file && file.name}</Caption1>
      </div>
    </section>
  );
};

export default ImageRegister;
