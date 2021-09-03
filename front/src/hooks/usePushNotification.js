import { useRef } from 'react';

const usePushNotification = () => {
  const notificationRef = useRef(null);
  const timerRef = useRef(null);

  if (!window.Notification) {
    return;
  }

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
    timerRef.current = setTimeout(() => {
      timerRef.current = null;

      notificationRef.current.close();
      notificationRef.current = null;
    }, timeout);
  };

  const setNotificationClickEvent = () => {
    notificationRef.current.onclick = (event) => {
      event.preventDefault();
      window.focus();
      notificationRef.current.close();
    };
  };

  const fireNotificationWithTimeout = (title, timeout, options = {}) => {
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
