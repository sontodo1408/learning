import { createI18n } from "vue-i18n";
import vi from "@/i18n/messages/vi";
import en from "@/i18n/messages/en";
import flagVn from "@/assets/flags/vn.svg";
import flagGb from "@/assets/flags/gb.svg";

// Key used to persist the user's chosen locale across sessions
const STORAGE_KEY = "locale";

// Locales shown by the language switcher, with their display name and flag icon.
// Flags are actual SVG images rather than flag emoji, since Windows fonts commonly
// render flag emoji as plain two-letter country codes instead of an image.
export const LOCALE_OPTIONS = [
  { code: "vi", name: "Vietnamese", flag: flagVn },
  { code: "en", name: "English", flag: flagGb },
];

// Supported locale codes, in the order shown by the language switcher
export const SUPPORTED_LOCALES = LOCALE_OPTIONS.map((option) => option.code);

// Default to the previously saved locale, falling back to Vietnamese
const savedLocale = localStorage.getItem(STORAGE_KEY);
const defaultLocale = SUPPORTED_LOCALES.includes(savedLocale) ? savedLocale : "vi";

const i18n = createI18n({
  legacy: false,
  globalInjection: true,
  locale: defaultLocale,
  fallbackLocale: "en",
  messages: { vi, en },
});

/** Switch the active locale and persist the choice */
export const setLocale = (locale) => {
  i18n.global.locale.value = locale;
  localStorage.setItem(STORAGE_KEY, locale);
};

export default i18n;
