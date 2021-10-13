import './ImageRegister.scss';

import { Caption1, Subtitle3 } from '../../core/Typography';
import React, { useRef } from 'react';

import { BsPlusCircle } from '@react-icons/all-files/bs/BsPlusCircle';
import { ModalError } from '..';
import PropTypes from 'prop-types';
import { useDefaultModal } from '../../contexts/DefaultModalProvider';

const ImageRegister = ({ file, setPreviewURL, setRegisterFile }) => {
  const { openModal } = useDefaultModal();
  const imageInputRef = useRef(null);

  const handleFile = () => {
    const newFile = imageInputRef.current.files[0];
    const filename = newFile.name;

    const imageReader = new FileReader();

    imageReader.readAsDataURL(newFile);
    imageReader.onload = () => {
      imageInputRef.current.value = null;

      setRegisterFile(newFile);
      setPreviewURL(filename, imageReader.result);
    };

    imageReader.onerror = () => {
      openModal(<ModalError>{imageReader.error}</ModalError>);
    };
  };

  return (
    <section className='input-register-container'>
      <Subtitle3>이미지 등록</Subtitle3>
      <div className='input-drag-drop'>
        <div className='drag-drop-zone'>
          <input
            ref={imageInputRef}
            type='file'
            accept='image/*'
            className='file-dropdown'
            onChange={handleFile}
            required
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

ImageRegister.propTypes = {
  file: PropTypes.shape({
    name: PropTypes.string,
    imagePath: PropTypes.string,
  }),
  setPreviewURL: PropTypes.func,
  setRegisterFile: PropTypes.func,
};

export default ImageRegister;
