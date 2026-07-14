<script setup>
import { ref, reactive, computed, inject, watch, watchEffect, onMounted, onBeforeUnmount } from 'vue';
import { useI18n } from 'vue-i18n';
import dialog from '@/utilities/dialog';
import { STUDY_HEADER_KEY, STUDY_SET_KEY } from '@/helpers/const';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const studyHeader = inject(STUDY_HEADER_KEY);
const studySet = inject(STUDY_SET_KEY);

// Time budget per question (no API/DB yet — see docs/04_Screen_Design/04_Test_Mode.md); the
// test's total time limit is this many seconds times the study set's question count
const SECONDS_PER_QUESTION = 30;
// Points awarded per question and number of distractor terms shown alongside the correct one
const POINTS_PER_QUESTION = 1;
const DISTRACTOR_COUNT = 3;

// 2) =============== VARIABLE REF     ===============
/** Map of question id -> the value the learner picked for it */
const answers = reactive({});
/** Seconds left before the (currently undesigned) time-up behavior would kick in; set once the
 * study set has loaded (see the watch below), 0 until then */
const remainingSeconds = ref(0);
/** Whether the test is currently being submitted */
const isSubmitting = ref(false);
/** Interval id for the countdown timer, cleared on unmount */
let timerId = null;

/** One multiple-choice question per study card: its definition as the prompt, its term as the
 * correct choice, plus up to DISTRACTOR_COUNT random terms from the other cards in the set */
const questions = computed(() => studySet.studyCards.map((card) => {
  const distractors = studySet.studyCards
    .filter((other) => other.id !== card.id)
    .map((other) => other.term)
    .sort(() => Math.random() - 0.5)
    .slice(0, DISTRACTOR_COUNT);
  return {
    id: card.id,
    category: studySet.title,
    points: POINTS_PER_QUESTION,
    text: card.definition,
    choices: [card.term, ...distractors].sort(() => Math.random() - 0.5),
    answer: card.term,
  };
}));

/** Number of questions answered so far */
const answeredCount = computed(() => Object.keys(answers).length);
/** Number of questions answered correctly so far */
const correctCount = computed(() => questions.value.filter((question) => answers[question.id] === question.answer).length);
/** Progress fill percentage for the progress bar; 0% while the study set hasn't loaded yet */
const progressPercent = computed(() => {
  if (!questions.value.length) { return 0; }
  return Math.round((answeredCount.value / questions.value.length) * 100);
});
/** Remaining time formatted as MM:SS */
const formattedTime = computed(() => {
  const minutes = String(Math.floor(remainingSeconds.value / 60)).padStart(2, '0');
  const seconds = String(remainingSeconds.value % 60).padStart(2, '0');
  return `${minutes}:${seconds}`;
});

// 3) =============== METHOD/FUNCTION  ===============
/** Record the learner's answer for one question */
const selectAnswer = (questionId, value) => { answers[questionId] = value; };

/** Submit the test; no scoring backend yet, so just show the correct-count scored locally */
const handleSubmitTest = async () => {
  isSubmitting.value = true;
  await dialog.showMessage(t('common.dialog.notice'),
    t('S0006.label.submitted', { correct: correctCount.value, total: questions.value.length }));
  isSubmitting.value = false;
};

// 4) =============== VUE JS LIFECYCLE ===============
onMounted(() => {
  timerId = setInterval(() => {
    if (remainingSeconds.value > 0) { remainingSeconds.value -= 1; }
  }, 1000);
});

onBeforeUnmount(() => { clearInterval(timerId); });

// Set (or reset, if the study set changes) the time limit once the question count is known:
// SECONDS_PER_QUESTION per question, e.g. 2 questions -> 1 minute
watch(() => questions.value.length, (length) => { remainingSeconds.value = length * SECONDS_PER_QUESTION; }, { immediate: true });

// Keep the shared header's progress bar in sync with this screen's own progress
watchEffect(() => { studyHeader.percent = progressPercent.value; });
</script>

<template>
  <q-page class="test-page">
    <div class="test-page__timer-row">
      <div class="test-page__timer">
        <q-icon name="schedule" size="18px" />
        {{ formattedTime }}
      </div>
    </div>

    <div class="test-page__progress-text">
      {{ t('S0006.label.questionProgress', { current: answeredCount, total: questions.length }) }}
      · {{ t('S0006.label.completed', { percent: progressPercent }) }}
    </div>

    <div v-for="(question, index) in questions" :key="question.id" class="test-question">
      <div class="test-question__tags">
        <q-chip dense square outline color="lime-1">{{ question.category }}</q-chip>
        <q-chip dense square outline color="lime-1">{{ t('S0006.label.points', { points: question.points }) }}</q-chip>
      </div>

      <div class="test-question__text">{{ index + 1 }}. {{ question.text }}</div>

      <div class="test-question__choices">
        <button v-for="choice in question.choices" :key="choice" type="button" class="test-choice"
          :class="{ 'test-choice--selected': answers[question.id] === choice }"
          @click="selectAnswer(question.id, choice)">
          {{ choice }}
        </button>
      </div>
    </div>

    <CBtn unelevated no-caps :label="t('S0006.btn.submit')" :loading="isSubmitting" class="test-page__submit"
      @click="handleSubmitTest" />
  </q-page>
</template>

<style lang="scss" scoped>
.test-page {
  &__timer-row {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 12px;
  }

  &__timer {
    display: flex;
    align-items: center;
    gap: 4px;
    padding: 6px 12px;
    border-radius: 999px;
    background-color: $lime-4;
    color: #3a2a22;
    font-weight: 700;
    white-space: nowrap;
  }

  &__progress-text {
    margin-bottom: 16px;
    font-size: 13px;
    font-weight: 600;
    color: rgba(#3a2a22, 0.6);
  }

  &__submit {
    display: block;
    width: 100%;
    margin-top: 8px;
    background-color: $lime-1;
    color: #fff;
    height: 44px;
  }
}

.test-question {
  background-color: #fff;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 8px 24px rgba($lime-1, 0.1);

  &__tags {
    display: flex;
    gap: 6px;
    margin-bottom: 10px;
  }

  &__text {
    font-size: 16px;
    color: #3a2a22;
    margin-bottom: 14px;
  }

  &__choices {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 10px;

    &--two-col {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }

    @media (max-width: 640px) {
      grid-template-columns: 1fr;
    }
  }
}

.test-choice {
  padding: 12px 14px;
  border: 2px solid $lime-5;
  border-radius: 10px;
  background-color: #fff;
  font-size: 14px;
  color: #3a2a22;
  cursor: pointer;
  text-align: left;
  transition: border-color 0.2s, background-color 0.2s;

  &--selected {
    border-color: $lime-1;
    background-color: $lime-5;
  }
}
</style>
