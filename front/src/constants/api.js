const BABBLE_URL = 'https://babble.gg';
const BASE_URL = 'https://api.babble.gg';
const TEST_URL = 'https://test-api.babble.gg';

const CONNECTION_URL = `${BASE_URL}/connection`;

const SUBSCRIBE_URL = {
  USERS: (roomId) => `/topic/rooms/${roomId}/users`,
  CHAT: (roomId) => `/topic/rooms/${roomId}/chat`,
};

const SEND_URL = {
  USERS: (roomId) => `/ws/rooms/${roomId}/users`,
  CHAT: (roomId) => `/ws/rooms/${roomId}/chat`,
};

export {
  BABBLE_URL,
  BASE_URL,
  TEST_URL,
  CONNECTION_URL,
  SUBSCRIBE_URL,
  SEND_URL,
};
