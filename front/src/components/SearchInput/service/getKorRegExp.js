import {
  BASE,
  FINALES,
  INITIALS,
  MEDIALS,
  MEDIAL_RANGE,
  MIXED,
} from './constant';

import escapeRegExp from './escapeRegExp';
import getPhonemes from './getPhonemes';

const getInitialSearchRegExp = (initial) => {
  const initialOffset = INITIALS.indexOf(initial);

  if (initialOffset !== -1) {
    const baseCode = initialOffset * MEDIALS.length * FINALES.length + BASE;
    return `[${String.fromCharCode(baseCode)}-${String.fromCharCode(
      baseCode + MEDIALS.length * FINALES.length - 1
    )}]`;
  }
  return initial;
};

const FUZZY = `__${parseInt('fuzzy', 36)}__`;
const IGNORE_SPACE = `__${parseInt('ignorespace', 36)}__`;

function getKorRegExp(
  search,
  {
    initialSearch = false,
    startsWith = false,
    endsWith = false,
    ignoreSpace = false,
    ignoreCase = true,
    global = false,
    fuzzy = false,
  }
) {
  let frontChars = search.split('');
  let lastChar = frontChars.slice(-1)[0];
  let lastCharPattern = '';

  const phonemes = getPhonemes(lastChar || '');

  // 마지막 글자가 한글인 경우만 수행
  if (phonemes.initialOffset !== -1) {
    frontChars = frontChars.slice(0, -1);
    const { initial, medial, finale, initialOffset, medialOffset } = phonemes;

    // 해당 초성으로 시작하는 첫번째 문자 : 가, 나, 다, ... , 하
    const baseCode = initialOffset * MEDIALS.length * FINALES.length + BASE;

    const patterns = [];

    switch (true) {
      // case 1: 종성으로 끝나는 경우 (받침이 있는 경우)
      case finale !== '': {
        // 마지막 글자
        patterns.push(lastChar);
        // 종성이 초성으로 사용 가능한 경우
        if (INITIALS.includes(finale)) {
          patterns.push(
            `${String.fromCharCode(
              baseCode + medialOffset * FINALES.length
            )}${getInitialSearchRegExp(finale)}`
          );
        }
        // 종성이 복합 자음인 경우, 두 개의 자음으로 분리하여 각각 받침과 초성으로 사용
        if (MIXED[finale]) {
          patterns.push(
            `${String.fromCharCode(
              baseCode +
                medialOffset * FINALES.length +
                FINALES.join('').search(MIXED[finale][0]) +
                1
            )}${getInitialSearchRegExp(MIXED[finale][1])}`
          );
        }
        break;
      }

      // case 2: 중성으로 끝나는 경우 (받침이 없는 경우)
      case medial !== '': {
        let from, to;
        // 중성이 복합 모음인 경우 범위를 확장하여 적용
        if (MEDIAL_RANGE[medial]) {
          from =
            baseCode +
            MEDIALS.join('').search(MEDIAL_RANGE[medial][0]) * FINALES.length;
          to =
            baseCode +
            MEDIALS.join('').search(MEDIAL_RANGE[medial][1]) * FINALES.length +
            FINALES.length -
            1;
        } else {
          from = baseCode + medialOffset * FINALES.length;
          to = from + FINALES.length - 1;
        }
        patterns.push(
          `[${String.fromCharCode(from)}-${String.fromCharCode(to)}]`
        );
        break;
      }

      // case 3: 초성만 입력된 경우
      case initial !== '': {
        patterns.push(getInitialSearchRegExp(initial));
        break;
      }

      default:
        break;
    }

    lastCharPattern =
      patterns.length > 1 ? `(${patterns.join('|')})` : patterns[0];
  }

  const glue = fuzzy ? FUZZY : ignoreSpace ? IGNORE_SPACE : '';
  const frontCharsPattern = initialSearch
    ? frontChars
        .map((char) =>
          char.search(/[ㄱ-ㅎ]/) !== -1
            ? getInitialSearchRegExp(char)
            : escapeRegExp(char)
        )
        .join(glue)
    : escapeRegExp(frontChars.join(glue));
  let pattern =
    (startsWith ? '^' : '') +
    frontCharsPattern +
    glue +
    lastCharPattern +
    (endsWith ? '$' : '');

  if (glue) {
    pattern = pattern
      .replace(RegExp(FUZZY, 'g'), '.*')
      .replace(RegExp(IGNORE_SPACE, 'g'), '\\s*');
  }

  return RegExp(pattern, (global ? 'g' : '') + (ignoreCase ? 'i' : ''));
}

export default getKorRegExp;
