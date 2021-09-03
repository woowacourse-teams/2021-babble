import { useEffect, useRef } from 'react';

const useTabNotification = () => {
  const timerRef = useRef(null);
  const notificationCountRef = useRef(0);
  const documentTitle = document.title;

  useEffect(() => {
    const resetTitle = () => {
      if (document.visibilityState === 'visible') {
        document.title = documentTitle;
        notificationCountRef.current = 0;

        clearInterval(timerRef.current);
        timerRef.current = null;
      }
    };

    document.addEventListener('visibilitychange', resetTitle);
    document.addEventListener('mozvisibilitychange', resetTitle);
    document.addEventListener('webkitvisibilitychange', resetTitle);
    document.addEventListener('msvisibilitychange', resetTitle);

    return () => {
      document.removeEventListener('visibilitychange', resetTitle);
      document.removeEventListener('mozvisibilitychange', resetTitle);
      document.removeEventListener('webkitvisibilitychange', resetTitle);
      document.removeEventListener('msvisibilitychange', resetTitle);

      if (timerRef.current) {
        clearInterval(timerRef.current);
        timerRef.current = null;
      }
    };
  }, []);

  const blinkTab = (title, notification, time) => {
    if (timerRef.current) {
      clearInterval(timerRef.current);
      timerRef.current = null;
    }

    timerRef.current = setInterval(() => {
      document.title = new Date().getSeconds() % 2 === 0 ? notification : title;
    }, time);
  };

  const showNotificationCount = ({ isBlinkable = true, blinkMessage = '' }) => {
    const pattern = /^\(\d+\)/; // TODO: constant로 분리
    notificationCountRef.current += 1;

    if (notificationCountRef.current === 0 || pattern.test(document.title)) {
      document.title = document.title.replace(
        pattern,
        notificationCountRef.current !== 0 &&
          `(${notificationCountRef.current})`
      );
    } else {
      document.title = `(${notificationCountRef.current}) ${document.title}`;
    }

    if (isBlinkable) {
      blinkTab(document.title, blinkMessage, 1000);
    }
  };

  const showNotification = (message) => {
    document.title = `${message} | ${document.title}`;
  };

  return { showNotificationCount, showNotification };
};

export default useTabNotification;
