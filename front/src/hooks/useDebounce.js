import { useRef } from 'react';

const useDebounce = () => {
  const timerRef = useRef(null);

  const debounce = (callback, delay) => {
    if (timerRef.current) clearTimeout(timerRef.current);

    timerRef.current = setTimeout(() => {
      callback();
    }, delay);
  };

  return { debounce };
};

export default useDebounce;
