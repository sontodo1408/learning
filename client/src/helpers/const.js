export const DIALOG_BTN = {
  YES: "Đồng ý",
  CLOSE: "Đóng",
  UNDERSTAND: "Tôi đã hiểu",
};

export const ROUTER_NAME = {
  LOGIN: "login",
  USER_HOME: "user-home",
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
// StudyLayout but populated by whichever Flashcard/Learn/Test screen is active.
export const STUDY_HEADER_KEY = "study-header";
