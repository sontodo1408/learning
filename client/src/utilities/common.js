import { toRaw } from 'vue';

// Origin (scheme + host + port) of the API, derived from VITE_API_URL, e.g. "http://localhost:8080".
// The server serves uploaded images at this origin's root (e.g. "/imgs/xxx.png"), not under "/api/v1".
const API_ORIGIN = new URL(import.meta.env.VITE_API_URL).origin;

/**
 * Resolves a server-relative image path (e.g. "/imgs/xxx.png") into an absolute URL usable as an
 * <img> src. Anything that isn't a server-relative path (already-absolute URL, blob: preview, null) is
 * passed through unchanged.
 */
export const resolveImageUrl = (path) => {
  if (!path?.startsWith('/')) { return path ?? null; }
  return `${API_ORIGIN}${path}`;
};

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
