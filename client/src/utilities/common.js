import { toRaw } from 'vue';

// Base URL images are served under, e.g. "http://localhost:8080/api/v1/imgs/". The server returns
// image paths as bare "<subdirectory>/<name>.<extension>" (e.g. "study/abc.png"), relative to this.
const IMAGE_BASE_URL = new URL('imgs/', import.meta.env.VITE_API_URL).toString();

/** Matches an already-absolute URL (http/https/blob/data) that should be used as-is. */
const ABSOLUTE_URL_PATTERN = /^[a-z][a-z0-9+.-]*:/i;

/**
 * Resolves a server-relative image path (e.g. "study/abc.png") into an absolute URL usable as an
 * <img> src. Anything that's already absolute (full URL, blob: preview, data: URI) or null/empty is
 * passed through unchanged.
 */
export const resolveImageUrl = (path) => {
  if (!path) { return null; }
  if (ABSOLUTE_URL_PATTERN.test(path)) { return path; }
  return `${IMAGE_BASE_URL}${path}`;
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
