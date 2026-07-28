// English messages. "common" holds shared chrome (nav, header, ...); every
// other key is a screen code (matching the screen's file name prefix, e.g.
// S0001_Home.vue -> "S0001"), holding a "btn" object for button text and a
// "label" object for standalone text/placeholders.
export default {
  common: {
    app: {
      name: 'Daily English with Mr.Son',
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
  userLayout: {
    label: {
      searchPlaceholder: 'Search study sets',
    },
  },
  // Part of speech labels for a study card's WORD_TYPE, shared across screens
  // (S0002's editor select and S0004's flashcard display) rather than owned by one screen.
  wordType: {
    noun: 'Noun',
    verb: 'Verb',
    adjective: 'Adjective',
    adverb: 'Adverb',
    pronoun: 'Pronoun',
    preposition: 'Preposition',
    conjunction: 'Conjunction',
    interjection: 'Interjection',
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
      wordTypePlaceholder: 'Word type',
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
      termTag: 'Term',
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
      questionProgress: '{current} / {total} answered',
      completed: '{percent}% Completed',
      points: '{points} pt',
      submitted: 'You answered {correct} / {total} correctly!',
    },
  },
  S0007: {
    label: {
      home: 'Home',
      mySets: 'My study sets',
      newStudySet: 'New study set',
      settings: 'Settings',
      help: 'Help',
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
  S0009: {
    label: {
      dailyVocabTitle: 'Daily English Vocabulary',
      dailyVocabSubtitle: 'Learn a little English every day',
      recentTitle: 'Recently visited',
      recentSubtitle: 'Pick up right where you left off',
    },
  },
  S0010: {
    label: {
      title: 'Search results for "{keyword}"',
      noResults: 'No matching study sets found',
    },
  },
  S0011: {
    label: {
      title: 'My study sets',
      empty: "You don't have any study sets yet",
    },
  },
  S0012: {
    btn: {
      goHome: 'Go to Home',
    },
    label: {
      code: '404',
      title: "Looks like you're lost",
      description: 'The page you are looking for is not available.',
    },
  },
  D0002: {
    btn: {
      addCard: 'Add card',
      save: 'Save',
    },
    label: {
      dialogTitle: 'New study set',
      editDialogTitle: 'Edit study set',
      titlePlaceholder: 'Study set title',
      descriptionPlaceholder: 'Description',
      isPublic: 'Make this study set public',
      cardsSection: 'Cards',
      termPlaceholder: 'Term',
      definitionPlaceholder: 'Definition',
      pronounceDefPlaceholder: 'Pronunciation',
      wordTypePlaceholder: 'Word type',
      saveSuccess: 'Study set saved successfully!',
    },
  },
};
