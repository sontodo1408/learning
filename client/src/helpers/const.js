export const DIALOG_BTN = {
  YES: "Đồng ý",
  NO: "Không",
  CLOSE: "Đóng",
  UNDERSTAND: "Tôi đã hiểu",
};

export const ROUTER_NAME = {
  LOGIN: "login",
  USER_HOME: "user-home",
  USER_SEARCH: "user-search",
  USER_MY_STUDY_SETS: "user-my-study-sets",
  HOME: "home",
  VIDEO_VOCAB: "video-vocab",
  FLASHCARD: "flashcard",
  LEARN: "learn",
  TEST: "test",
};

// Spring Security authority strings (must match the server's ERole enum)
export const ROLE = {
  ADMIN: "ROLE_ADMIN",
  USER: "ROLE_USER",
};

// Provide/inject key for the study header (title/tags/progress) rendered by
// S0008_Study but populated by whichever Flashcard/Learn/Test screen is active.
export const STUDY_HEADER_KEY = "study-header";

// Provide/inject key for the study set (with its studyCards) fetched once by
// S0008_Study from the :setId route param, shared with whichever mode screen is active.
export const STUDY_SET_KEY = "study-set";

// Part of speech ("loại từ") for a study card. Values are ordinals and must match the
// server's EWordType enum order exactly (it's (de)serialized as a plain integer).
export const WORD_TYPE = {
  NOUN: 0,
  VERB: 1,
  ADJECTIVE: 2,
  ADVERB: 3,
  PRONOUN: 4,
  PREPOSITION: 5,
  CONJUNCTION: 6,
  INTERJECTION: 7,
};

// i18n key (under the shared "wordType" namespace, see messages/*.js) for each WORD_TYPE ordinal.
export const WORD_TYPE_LABEL_KEY = {
  [WORD_TYPE.NOUN]: "wordType.noun",
  [WORD_TYPE.VERB]: "wordType.verb",
  [WORD_TYPE.ADJECTIVE]: "wordType.adjective",
  [WORD_TYPE.ADVERB]: "wordType.adverb",
  [WORD_TYPE.PRONOUN]: "wordType.pronoun",
  [WORD_TYPE.PREPOSITION]: "wordType.preposition",
  [WORD_TYPE.CONJUNCTION]: "wordType.conjunction",
  [WORD_TYPE.INTERJECTION]: "wordType.interjection",
};
