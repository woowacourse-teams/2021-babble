import './MainImage.scss';

import PropTypes from 'prop-types';
import React from 'react';

const MainImage = ({
  imageSrc = 'https://images.igdb.com/igdb/image/upload/t_1080p/co254s.jpg',
}) => {
  return (
    <section className='main-image-container'>
      <div className='gradient' />
      <img className='main-image' src={imageSrc} />
    </section>
  );
};

MainImage.propTypes = {
  imageSrc: PropTypes.string,
};

export default MainImage;
