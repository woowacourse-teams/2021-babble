import { useRef } from 'react';

const useThrottle = () => {
  const timerRef = useRef(null);

  const throttle = (callback, delay) => {
    if (!timerRef.current) {
      timerRef.current = setTimeout(() => {
        callback();

        clearTimeout(timerRef.current);
        timerRef.current = null;
      }, delay);
    }
  };

  return { throttle };
};

export default useThrottle;
