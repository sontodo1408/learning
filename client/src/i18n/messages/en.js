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
      confirm: 'Confirm',
    },
    nav: {
      dashboard: 'Dashboard',
      category: 'Category',
      transaction: 'Expense items',
      history: 'History',
      videoVocab: 'Learn Vocab Video',
      logout: 'Log out',
      signIn: 'Sign in',
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
      selectStudySet: 'Select study set',
      save: 'Save',
    },
    label: {
      studySetTitle: 'Study Set Title',
      termPlaceholder: 'Term',
      pronounceDefPlaceholder: 'Pronunciation',
      definitionPlaceholder: 'Definition',
      ringing: 'Ring ring!',
      selectStudySetTitle: 'Select study set',
      noStudySets: 'No study sets yet',
      studySetCardCount: '{count} cards',
      saveSuccess: 'Study set saved successfully!',
      saveConfirm: 'Are you sure you want to save this study set?',
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
      home: 'Home',
      mySets: 'My study sets',
      newStudySet: 'New study set',
      settings: 'Settings',
      help: 'Help',
      dailyVocabTitle: 'Daily English Vocabulary',
      dailyVocabSubtitle: 'Learn a little English every day',
      recentTitle: 'Recently visited',
      recentSubtitle: 'Pick up right where you left off',
    },
  },
  S0008: {
    label: {
      flashcards: 'Flashcards',
      learn: 'Learn',
      test: 'Test',
      newStudySet: 'New Study Set',
      settings: 'Settings',
      help: 'Help',
    },
  },
};
