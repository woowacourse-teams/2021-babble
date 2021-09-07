import { useEffect, useRef } from 'react';

import { NOTIFICATION_COUNT_PATTERN } from '../constants/regex';

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
      }
    };
  }, []);

  const blinkTab = (title, notification, time) => {
    if (timerRef.current) {
      clearInterval(timerRef.current);
    }

    timerRef.current = setInterval(() => {
      document.title = new Date().getSeconds() % 2 === 0 ? notification : title;
    }, time);
  };

  const showNotificationCount = ({ isBlinkable = true, blinkMessage = '' }) => {
    // TODO: constant로 분리
    notificationCountRef.current += 1;

    if (
      notificationCountRef.current === 0 ||
      NOTIFICATION_COUNT_PATTERN.test(document.title)
    ) {
      document.title = document.title.replace(
        NOTIFICATION_COUNT_PATTERN,
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
