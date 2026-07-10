// Vietnamese messages. "common" holds shared chrome (nav, header, ...); every
// other key is a screen code (matching the screen's file name prefix, e.g.
// S0001_Home.vue -> "S0001"), holding a "btn" object for button text and a
// "label" object for standalone text/placeholders.
export default {
  common: {
    app: {
      name: 'Daily English with Son',
    },
    dialog: {
      notice: 'Thông báo',
    },
    nav: {
      dashboard: 'Dashboard',
      category: 'Danh mục',
      transaction: 'Các mục chi tiêu',
      history: 'Lịch sử',
      videoVocab: 'Học từ vựng qua Video',
      logout: 'Đăng xuất',
    },
  },
  S0001: {
    label: {
      title: 'Trang chủ',
    },
  },
  S0002: {
    btn: {
      addCard: 'Thêm thẻ',
      logData: 'Xem dữ liệu',
      play: 'Bắt đầu',
      stop: 'Dừng',
    },
    label: {
      termPlaceholder: 'Từ vựng',
      definitionPlaceholder: 'Định nghĩa',
      ringing: 'Reng reng!',
    },
  },
  S0003: {
    btn: {
      submit: 'Đăng nhập',
    },
    label: {
      title: 'Đăng nhập',
      greeting: 'Chào bạn quay lại',
      usernamePlaceholder: 'Tên đăng nhập',
      passwordPlaceholder: 'Mật khẩu',
      forgotPassword: 'Quên mật khẩu?',
    },
  },
  // Shared chrome for StudyLayout (Flashcard/Learn/Test mode sidebar + tab bar)
  study: {
    nav: {
      flashcards: 'Thẻ ghi nhớ',
      learn: 'Học',
      test: 'Kiểm tra',
      newStudySet: 'Học phần mới',
      settings: 'Cài đặt',
      help: 'Trợ giúp',
    },
  },
  S0004: {
    btn: {
      prev: 'Trước',
      next: 'Tiếp',
      flip: 'Lật thẻ',
    },
    label: {
      tapToReveal: 'Chạm để xem đáp án',
      cardProgress: 'Thẻ {current} / {total}',
    },
  },
  S0005: {
    btn: {
      dontKnow: 'Không biết?',
      hint: 'Gợi ý',
      report: 'Báo lỗi',
    },
    label: {
      definitionTag: 'Định nghĩa',
      progress: '{current} / {total} từ',
      hintText: 'Đáp án bắt đầu bằng chữ "{letter}"',
      roundComplete: 'Bạn đã hoàn thành lượt học này!',
      reportReceived: 'Đã ghi nhận báo lỗi của bạn, cảm ơn bạn!',
    },
  },
  S0006: {
    btn: {
      submit: 'Nộp bài',
    },
    label: {
      multipleChoice: 'Trắc nghiệm',
      trueFalse: 'Đúng / Sai',
      true: 'Đúng',
      false: 'Sai',
      questionProgress: 'Đã trả lời {current} / {total} câu',
      completed: '{percent}% hoàn thành',
      points: '{points} điểm',
      submitted: 'Bạn đã nộp bài thành công!',
    },
  },
};
