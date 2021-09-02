import { useRef } from 'react';

const useThrottle = () => {
  const timerRef = useRef(null);

  const throttle = (callback, delay) => {
    if (!timerRef.current) {
      timerRef.current = setTimeout(() => {
        callback();
        timerRef.current = null;
      }, delay);
    }
  };

  return { throttle };
};

export default useThrottle;
