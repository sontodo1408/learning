import { toRaw } from 'vue';

export const clone = (item, defaultValue = null) => {
  try {
    return structuredClone(toRaw(item));
  } catch {
    return defaultValue;
  }
};

export const getMoneyLabel = (amount) => {
  if (!amount) {
    return '';
  }

  const raw = String(amount).replaceAll(',', '');
  const number = Number(raw);
  if (Number.isNaN(number)) {
    return '0';
  }

  return number.toLocaleString('en-US');
};

export const isEqual = (a, b) => {
  if (a === b) return true;

  if (typeof a !== 'object' || typeof b !== 'object' || a === null || b === null) {
    return false;
  }

  const keysA = Object.keys(a);
  const keysB = Object.keys(b);

  if (keysA.length !== keysB.length) {
    return false;
  }

  for (const key of keysA) {
    if (!keysB.includes(key)) {
      return false;
    }

    if (!isEqual(a[key], b[key])) {
      return false;
    }
  }

  return true;
};
