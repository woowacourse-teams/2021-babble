const PATTERNS = {
  NOTIFICATION_COUNT: /^\(\d+\)/,
  KOREAN: /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+/g,
  SPECIAL_CHARACTERS: /[^0-9|ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z|A-Z]+/g,
  SPECIAL_CHARACTERS_WITHOUT_SPACE: /[^0-9|ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z|A-Z|\s]+/g,
  SPACE: /\s/g,
};

export { PATTERNS };
