import { useRef } from 'react';
import useThrottle from './useThrottle';

const usePushNotification = () => {
  const { throttle } = useThrottle();
  const notificationRef = useRef(null);

  if (!Notification) return;

  if (Notification.permission !== 'granted') {
    try {
      Notification.requestPermission().then((permission) => {
        if (permission !== 'granted') return;
      });
    } catch (error) {
      if (error instanceof TypeError) {
        Notification.requestPermission((permission) => {
          if (permission !== 'granted') return;
        });
      } else {
        console.error(error);
      }
    }
  }

  const setNotificationTimer = (timeout) => {
    throttle(() => {
      if (notificationRef.current) {
        notificationRef.current.close();
        notificationRef.current = null;
      }
    }, timeout);
  };

  const setNotificationClickEvent = () => {
    notificationRef.current.onclick = (event) => {
      event.preventDefault();
      window.focus();

      if (notificationRef.current) {
        notificationRef.current.close();
        notificationRef.current = null;
      }
    };
  };

  const fireNotificationWithTimeout = (title, timeout, options = {}) => {
    if (Notification.permission !== 'granted') return;

    const newOption = {
      badge: 'https://babble.gg/img/logos/babble-speech-bubble.png',
      icon: 'https://babble.gg/img/logos/babble-speech-bubble.png',
      ...options,
    };

    if (!notificationRef.current) {
      setNotificationTimer(timeout);

      notificationRef.current = new Notification(title, newOption);
      setNotificationClickEvent();
    }
  };

  return { fireNotificationWithTimeout };
};

export default usePushNotification;
