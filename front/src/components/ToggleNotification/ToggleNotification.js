import './ToggleNotification.scss';

import {
  IoBanOutline,
  IoNotifications,
  IoNotificationsOff,
} from 'react-icons/io5';

import { Caption2 } from '../../core/Typography';
import React from 'react';
import { usePushNotification } from '../../contexts/PushNotificationProvider';

const ToggleNotification = () => {
  const { isNotificationOn, permission, toggleNotification } =
    usePushNotification();

  return (
    <div className='notification-switch-container'>
      {permission === 'granted' ? (
        <button className='notification-switch' onClick={toggleNotification}>
          {isNotificationOn ? (
            <>
              <IoNotifications size='18px' color='#ff005c' />
              <Caption2>
                채팅 알림 <span className='notification-on'>켜짐</span>
              </Caption2>
            </>
          ) : (
            <>
              <IoNotificationsOff size='18px' color='#969696' />
              <Caption2>
                채팅 알림 <span className='notification-off'>꺼짐</span>
              </Caption2>
            </>
          )}
        </button>
      ) : (
        <div className='ban-container'>
          <div className='ban'>
            <IoNotifications size='18px' />
            <IoBanOutline size='24px' color='#ff0000' />
          </div>
          <Caption2>
            채팅 알림 <span className='notification-banned'>차단됨</span>
          </Caption2>
        </div>
      )}
    </div>
  );
};

export default ToggleNotification;
