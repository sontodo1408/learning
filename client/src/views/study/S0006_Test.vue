<script setup>
import { ref, reactive, computed, inject, watchEffect, onMounted, onBeforeUnmount } from 'vue';
import { useI18n } from 'vue-i18n';
import dialog from '@/utilities/dialog';
import { STUDY_HEADER_KEY } from '@/helpers/const';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const studyHeader = inject(STUDY_HEADER_KEY);

// Mock time limit for the test (no API/DB yet — see docs/04_Screen_Design/04_Test_Mode.md)
const INITIAL_SECONDS = 30 * 60;

/** Mock questions for the test */
const QUESTIONS = [
  {
    id: 1,
    type: 'multiple_choice',
    category: 'Cellular Biology',
    points: 1,
    text: 'Which organelle is responsible for producing energy (ATP) in a cell?',
    choices: ['Mitochondria', 'Nucleus', 'Ribosome', 'Golgi apparatus'],
  },
  {
    id: 2,
    type: 'true_false',
    category: 'Cellular Biology',
    points: 1,
    text: "The nucleus contains the cell's genetic material (DNA).",
  },
  {
    id: 3,
    type: 'multiple_choice',
    category: 'Anatomy',
    points: 1,
    text: 'Which structure controls what enters and exits the cell?',
    choices: ['Cell wall', 'Cell membrane', 'Cytoskeleton', 'Nuclear envelope'],
  },
  {
    id: 4,
    type: 'true_false',
    category: 'Anatomy',
    points: 1,
    text: 'Ribosomes are the site of protein synthesis within a cell.',
  },
];

// 2) =============== VARIABLE REF     ===============
/** Map of question id -> the value the learner picked for it */
const answers = reactive({});
/** Seconds left before the (currently undesigned) time-up behavior would kick in */
const remainingSeconds = ref(INITIAL_SECONDS);
/** Whether the test is currently being submitted */
const isSubmitting = ref(false);
/** Interval id for the countdown timer, cleared on unmount */
let timerId = null;

/** Number of questions answered so far */
const answeredCount = computed(() => Object.keys(answers).length);
/** Progress fill percentage for the progress bar */
const progressPercent = computed(() => Math.round((answeredCount.value / QUESTIONS.length) * 100));
/** Remaining time formatted as MM:SS */
const formattedTime = computed(() => {
  const minutes = String(Math.floor(remainingSeconds.value / 60)).padStart(2, '0');
  const seconds = String(remainingSeconds.value % 60).padStart(2, '0');
  return `${minutes}:${seconds}`;
});

// 3) =============== METHOD/FUNCTION  ===============
/** Record the learner's answer for one question */
const selectAnswer = (questionId, value) => { answers[questionId] = value; };

/** Submit the test; no scoring backend yet, so just acknowledge the submission */
const handleSubmitTest = async () => {
  isSubmitting.value = true;
  await dialog.showMessage(t('common.dialog.notice'), t('S0006.label.submitted'));
  isSubmitting.value = false;
};

// 4) =============== VUE JS LIFECYCLE ===============
onMounted(() => {
  timerId = setInterval(() => {
    if (remainingSeconds.value > 0) { remainingSeconds.value -= 1; }
  }, 1000);
  studyHeader.title = 'Biology 101 Midterm';
  studyHeader.tags = [];
});

onBeforeUnmount(() => { clearInterval(timerId); });

// Keep the shared header's progress bar in sync with this screen's own progress
watchEffect(() => { studyHeader.percent = progressPercent.value; });
</script>

<template>
  <q-page class="test-page tw:p-4">
    <div class="test-page__timer-row">
      <div class="test-page__timer">
        <q-icon name="schedule" size="18px" />
        {{ formattedTime }}
      </div>
    </div>

    <div class="test-page__progress-text">
      {{ t('S0006.label.questionProgress', { current: answeredCount, total: QUESTIONS.length }) }}
      · {{ t('S0006.label.completed', { percent: progressPercent }) }}
    </div>

    <div v-for="(question, index) in QUESTIONS" :key="question.id" class="test-question">
      <div class="test-question__tags">
        <q-chip dense square color="lime-5" text-color="lime-1">
          {{ question.type === 'multiple_choice' ? t('S0006.label.multipleChoice') : t('S0006.label.trueFalse') }}
        </q-chip>
        <q-chip dense square outline color="grey-6">{{ question.category }}</q-chip>
        <q-chip dense square outline color="grey-6">{{ t('S0006.label.points', { points: question.points }) }}</q-chip>
      </div>

      <div class="test-question__text">{{ index + 1 }}. {{ question.text }}</div>

      <div v-if="question.type === 'multiple_choice'" class="test-question__choices">
        <button v-for="choice in question.choices" :key="choice" type="button" class="test-choice"
          :class="{ 'test-choice--selected': answers[question.id] === choice }" @click="selectAnswer(question.id, choice)">
          {{ choice }}
        </button>
      </div>

      <div v-else class="test-question__choices test-question__choices--two-col">
        <button type="button" class="test-choice" :class="{ 'test-choice--selected': answers[question.id] === true }"
          @click="selectAnswer(question.id, true)">
          {{ t('S0006.label.true') }}
        </button>
        <button type="button" class="test-choice" :class="{ 'test-choice--selected': answers[question.id] === false }"
          @click="selectAnswer(question.id, false)">
          {{ t('S0006.label.false') }}
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
