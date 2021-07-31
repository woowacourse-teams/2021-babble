import { useEffect, useRef } from 'react';

const useUpdateEffect = (effect, dependencies) => {
  const isFirstMount = useRef(true);

  useEffect(() => {
    if (!isFirstMount.current) effect();
    else isFirstMount.current = false;
  }, dependencies);
};

export default useUpdateEffect;
