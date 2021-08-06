import './Slider.scss';

import React, { useEffect, useRef, useState } from 'react';

import { Caption2 } from '../../core/Typography';
import PropTypes from 'prop-types';

const Slider = ({ imageList }) => {
  const slideRef = useRef(null);
  const intervalRef = useRef(null);
  const imageCount = imageList.length;
  const [currentIndex, setCurrentIndex] = useState(0);

  const nextSlide = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === imageCount - 1 ? 0 : prevIndex + 1
    );
  };

  const activeSlide = (id) => {
    setCurrentIndex((prevIndex) => (prevIndex !== id ? id : prevIndex));

    if (intervalRef.current) {
      clearInterval(intervalRef.current);

      intervalRef.current = setInterval(() => {
        nextSlide();
      }, 5000);
    }
  };

  useEffect(() => {
    slideRef.current.style.width = `${imageCount * 100}%`;

    intervalRef.current = setInterval(() => {
      nextSlide();
    }, 5000);

    return () => {
      clearInterval(intervalRef.current);
    };
  }, []);

  useEffect(() => {
    if (currentIndex < imageCount) {
      slideRef.current.style.transform = `translateX(${
        currentIndex * -(100 / imageCount)
      }%)`;
    }
  }, [currentIndex]);

  return (
    <section className='slider-container'>
      <section className='show'>
        <div className='page-index-container'>
          <div className='index'>
            <Caption2>
              {currentIndex + 1} / {imageCount}
            </Caption2>
          </div>
        </div>
        <ul className='slide-list' ref={slideRef}>
          {imageList.map(({ id, info, src }) => (
            <li className='slide' key={id}>
              <img src={src} alt={info} />
            </li>
          ))}
        </ul>
      </section>
      <section className='swiper-pagination-container'>
        <div className='dots-container'>
          {imageList.map(({ id }) => (
            <span
              key={id}
              className='dot-wrapper'
              onClick={() => activeSlide(id)}
            >
              <span
                className={`dot ${id === currentIndex ? 'current' : ''}`}
                data-index={id + 1}
              ></span>
            </span>
          ))}
        </div>
      </section>
    </section>
  );
};

Slider.propTypes = {
  imageList: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number,
      info: PropTypes.string,
      src: PropTypes.string,
    })
  ),
};

export default Slider;
