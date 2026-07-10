<script setup>
import { ref, computed, inject, watchEffect, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import dialog from '@/utilities/dialog';
import { STUDY_HEADER_KEY } from '@/helpers/const';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const studyHeader = inject(STUDY_HEADER_KEY);

// How long the selected choice stays highlighted before advancing to the next question
const ADVANCE_DELAY_MS = 600;

/** Mock questions for the round (no API/DB yet — see docs/04_Screen_Design/03_Learn_Mode.md) */
const QUESTIONS = [
  {
    definition: 'The organelle responsible for producing energy (ATP) in a cell.',
    choices: ['Mitochondria', 'Nucleus', 'Ribosome', 'Golgi apparatus'],
    answer: 'Mitochondria',
  },
  {
    definition: "The organelle that contains the cell's genetic material (DNA).",
    choices: ['Cytoplasm', 'Nucleus', 'Lysosome', 'Vacuole'],
    answer: 'Nucleus',
  },
  {
    definition: 'The site of protein synthesis within a cell.',
    choices: ['Ribosome', 'Peroxisome', 'Centriole', 'Golgi apparatus'],
    answer: 'Ribosome',
  },
  {
    definition: 'The outer boundary that controls what enters and exits the cell.',
    choices: ['Cell wall', 'Cell membrane', 'Cytoskeleton', 'Nuclear envelope'],
    answer: 'Cell membrane',
  },
];

// 2) =============== VARIABLE REF     ===============
/** Index of the question currently shown */
const currentIndex = ref(0);
/** The choice the learner just picked, kept highlighted until the round advances */
const selectedChoice = ref(null);
/** Whether the hint (first letter of the answer) is revealed for the current question */
const hintRevealed = ref(false);

/** The question currently shown */
const currentQuestion = computed(() => QUESTIONS[currentIndex.value]);
/** Progress fill percentage for the progress bar */
const progressPercent = computed(() => Math.round(((currentIndex.value + 1) / QUESTIONS.length) * 100));

// 3) =============== METHOD/FUNCTION  ===============
/** Move to the next question, or announce the round is complete once the last one is answered */
const advance = () => {
  if (currentIndex.value >= QUESTIONS.length - 1) {
    dialog.showMessage(t('common.dialog.notice'), t('S0005.label.roundComplete'));
    return;
  }
  currentIndex.value += 1;
  selectedChoice.value = null;
  hintRevealed.value = false;
};

/** Highlight the picked choice, then advance after a short delay */
const selectChoice = (choice) => {
  if (selectedChoice.value) { return; }
  selectedChoice.value = choice;
  setTimeout(advance, ADVANCE_DELAY_MS);
};

/** Skip the current question without picking a choice (desktop-only "Don't know?" action) */
const skipQuestion = () => { advance(); };

/** Reveal the first letter of the answer as a hint (mobile-only "Hint" action) */
const showHint = () => { hintRevealed.value = true; };

/** Report the current question as problematic; no report backend yet, so just acknowledge it */
const reportQuestion = () => { dialog.showMessage(t('common.dialog.notice'), t('S0005.label.reportReceived')); };

// 4) =============== VUE JS LIFECYCLE ===============
onMounted(() => {
  studyHeader.title = 'Advanced Biology: Cellular Respiration';
  studyHeader.tags = ['Round 1: Initial Learning'];
});

// Keep the shared header's progress bar in sync with this screen's own progress
watchEffect(() => { studyHeader.percent = progressPercent.value; });
</script>

<template>
  <q-page class="learn-page">
    <div class="learn-page__progress-text">
      {{ t('S0005.label.progress', { current: currentIndex + 1, total: QUESTIONS.length }) }}
    </div>

    <div class="learn-question">
      <q-chip dense square color="lime-5" text-color="lime-1" class="learn-question__tag">
        {{ t('S0005.label.definitionTag') }}
      </q-chip>
      <div class="learn-question__text">{{ currentQuestion.definition }}</div>
      <div v-if="hintRevealed" class="learn-question__hint">
        {{ t('S0005.label.hintText', { letter: currentQuestion.answer[0] }) }}
      </div>
    </div>

    <div class="learn-choices">
      <button v-for="(choice, index) in currentQuestion.choices" :key="choice" type="button" class="learn-choice"
        :class="{ 'learn-choice--selected': selectedChoice === choice }" @click="selectChoice(choice)">
        <span class="learn-choice__badge">{{ index + 1 }}</span>
        <span>{{ choice }}</span>
      </button>
    </div>

    <div class="learn-actions">
      <CBtn flat no-caps :label="t('S0005.btn.dontKnow')" @click="skipQuestion" />
      <div class="tw:flex tw:gap-2">
        <CBtn flat no-caps icon="lightbulb" :label="t('S0005.btn.hint')" @click="showHint" />
        <CBtn flat no-caps icon="flag" :label="t('S0005.btn.report')" @click="reportQuestion" />
      </div>
    </div>
  </q-page>
</template>

<style lang="scss" scoped>
.learn-page {
  &__progress-text {
    margin-bottom: 16px;
    font-size: 13px;
    font-weight: 600;
    color: rgba(#3a2a22, 0.6);
  }
}

.learn-question {
  background-color: #fff;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 8px 24px rgba($lime-1, 0.12);

  &__tag {
    margin-bottom: 12px;
  }

  &__text {
    font-size: 19px;
    color: #3a2a22;
  }

  &__hint {
    margin-top: 12px;
    font-size: 14px;
    font-style: italic;
    color: rgba(#3a2a22, 0.6);
  }
}

.learn-choices {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
  margin-bottom: 20px;

  @media (max-width: 640px) {
    grid-template-columns: 1fr;
  }
}

.learn-choice {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border: 2px solid $lime-5;
  border-radius: 12px;
  background-color: #fff;
  font-size: 15px;
  color: #3a2a22;
  cursor: pointer;
  text-align: left;
  transition: border-color 0.2s, background-color 0.2s;

  &__badge {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 22px;
    height: 22px;
    border-radius: 999px;
    background-color: $lime-5;
    color: $lime-1;
    font-size: 12px;
    font-weight: 700;
  }

  &--selected {
    border-color: $lime-1;
    background-color: $lime-5;
  }
}

.learn-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
