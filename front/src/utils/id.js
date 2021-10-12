import { parse, v4 } from 'uuid';

export const getNumberId = () => {
  const idString = v4();
  const bytes = parse(idString);
  const id = Number(
    [...bytes].map((v) => v.toString(10).padStart(2, '0')).join('')
  );

  return id;
};
