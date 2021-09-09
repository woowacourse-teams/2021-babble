import React, { createContext, useContext, useRef, useState } from 'react';

import { BABBLE_URL } from '../constants/api';
import { PERMISSION } from '../constants/notification';
import PropTypes from 'prop-types';
import useThrottle from '../hooks/useThrottle';

const PushNotificationContext = createContext();

const PushNotificationProvider = ({ children }) => {
  const [isNotificationOn, setIsNotificationOn] = useState(true);
  const [permission, setPermission] = useState(Notification.permission);

  const notificationRef = useRef(null);

  const { throttle } = useThrottle();

  if (!Notification) return;

  if (permission !== PERMISSION.GRANTED) {
    try {
      Notification.requestPermission().then((permission) => {
        if (permission !== PERMISSION.GRANTED) return;
        setPermission(permission);
      });
    } catch (error) {
      if (error instanceof TypeError) {
        Notification.requestPermission((permission) => {
          if (permission !== PERMISSION.GRANTED) return;
          setPermission(permission);
        });
      } else {
        console.error(error);
      }
    }
  }

  const toggleNotification = () => {
    setIsNotificationOn((prevState) => !prevState);
  };

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
    if (permission !== PERMISSION.GRANTED) return;

    const newOption = {
      badge: `${BABBLE_URL}/img/logos/babble-speech-bubble.png`,
      icon: `${BABBLE_URL}/img/logos/babble-speech-bubble.png`,
      vibrate: true,
      ...options,
    };

    if (!notificationRef.current) {
      setNotificationTimer(timeout);

      notificationRef.current = new Notification(title, newOption);
      setNotificationClickEvent();
    }
  };

  return (
    <PushNotificationContext.Provider
      value={{
        isNotificationOn,
        permission,
        toggleNotification,
        fireNotificationWithTimeout,
      }}
    >
      {children}
    </PushNotificationContext.Provider>
  );
};

PushNotificationProvider.propTypes = {
  children: PropTypes.node,
};

export const usePushNotification = () => {
  return useContext(PushNotificationContext);
};

export default PushNotificationProvider;
