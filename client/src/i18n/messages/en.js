// English messages. "common" holds shared chrome (nav, header, ...); every
// other key is a screen code (matching the screen's file name prefix, e.g.
// S0001_Home.vue -> "S0001"), holding a "btn" object for button text and a
// "label" object for standalone text/placeholders.
export default {
  common: {
    app: {
      name: 'Daily English with Son',
    },
    dialog: {
      notice: 'Notice',
    },
    nav: {
      dashboard: 'Dashboard',
      category: 'Category',
      transaction: 'Expense items',
      history: 'History',
      videoVocab: 'Learn Vocab Video',
      logout: 'Log out',
    },
  },
  S0001: {
    label: {
      title: 'Home',
    },
  },
  S0002: {
    btn: {
      addCard: 'Add card',
      logData: 'Log data',
      play: 'Start',
      stop: 'Stop',
    },
    label: {
      termPlaceholder: 'Term',
      definitionPlaceholder: 'Definition',
      ringing: 'Ring ring!',
    },
  },
  S0003: {
    btn: {
      submit: 'Log in',
      loginWithGoogle: 'Log in with Google',
    },
    label: {
      title: 'Login',
      greeting: 'Welcome back',
      usernamePlaceholder: 'Username',
      passwordPlaceholder: 'Password',
      forgotPassword: 'Forgot password?',
      orDivider: 'Or',
    },
  },
  // Shared chrome for StudyLayout (Flashcard/Learn/Test mode sidebar + tab bar)
  study: {
    nav: {
      flashcards: 'Flashcards',
      learn: 'Learn',
      test: 'Test',
      newStudySet: 'New Study Set',
      settings: 'Settings',
      help: 'Help',
    },
  },
  S0004: {
    btn: {
      prev: 'Prev',
      next: 'Next',
      flip: 'Flip',
    },
    label: {
      tapToReveal: 'Tap to reveal',
      cardProgress: 'Card {current} / {total}',
    },
  },
  S0005: {
    btn: {
      dontKnow: "Don't know?",
      hint: 'Hint',
      report: 'Report',
    },
    label: {
      definitionTag: 'Definition',
      progress: '{current} / {total} Terms',
      hintText: 'The answer starts with "{letter}"',
      roundComplete: "You've completed this round!",
      reportReceived: 'Your report has been received, thank you!',
    },
  },
  S0006: {
    btn: {
      submit: 'Submit Test',
    },
    label: {
      multipleChoice: 'Multiple Choice',
      trueFalse: 'True / False',
      true: 'True',
      false: 'False',
      questionProgress: '{current} / {total} answered',
      completed: '{percent}% Completed',
      points: '{points} pt',
      submitted: 'Your test has been submitted!',
    },
  },
  S0007: {
    label: {
      title: 'Home',
    },
  },
};
