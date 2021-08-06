import { useRef } from 'react';

const useDebounce = (delay) => {
  const timerRef = useRef(null);

  const debounce = (callback) => {
    if (timerRef.current) clearTimeout(timerRef.current);

    timerRef.current = setTimeout(() => {
      callback();
    }, delay);
  };

  return { debounce };
};

export default useDebounce;
