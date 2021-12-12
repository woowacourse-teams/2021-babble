export const setSessionStorage = (key, value) => {
  sessionStorage.setItem(key, value);
};

export const getSessionStorage = (key) => {
  return sessionStorage.getItem(key);
};

export const removeFromSessionStorage = (key) => {
  sessionStorage.removeItem(key);
};

export const setLocalStorage = (key, value) => {
  localStorage.setItem(key, value);
};

export const getLocalStorage = (key) => {
  return localStorage.getItem(key);
};

export const removeFromLocalStorage = (key) => {
  localStorage.removeItem(key);
};
