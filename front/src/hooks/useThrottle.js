import { useRef } from 'react';

const useThrottle = (delay) => {
  const timerRef = useRef(null);

  const throttle = (callback) => {
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
