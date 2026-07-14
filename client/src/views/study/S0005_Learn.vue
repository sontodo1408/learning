<script setup>
import { ref, computed, inject, watchEffect } from 'vue';
import { useI18n } from 'vue-i18n';
import dialog from '@/utilities/dialog';
import { STUDY_HEADER_KEY, STUDY_SET_KEY } from '@/helpers/const';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const studyHeader = inject(STUDY_HEADER_KEY);
const studySet = inject(STUDY_SET_KEY);

// How long the selected choice stays highlighted before advancing to the next question
const ADVANCE_DELAY_MS = 600;
// Number of choices shown per question (the correct term + this many distractor terms)
const DISTRACTOR_COUNT = 3;

// 2) =============== VARIABLE REF     ===============
/** Index of the question currently shown */
const currentIndex = ref(0);
/** The choice the learner just picked, kept highlighted until the round advances */
const selectedChoice = ref(null);
/** Whether the hint (first letter of the answer) is revealed for the current question */
const hintRevealed = ref(false);

/** One multiple-choice question per study card: its term as the prompt, its definition as the
 * correct choice, plus up to DISTRACTOR_COUNT random definitions from the other cards in the set */
const questions = computed(() => studySet.studyCards.map((card) => {
  const distractors = studySet.studyCards
    .filter((other) => other.id !== card.id)
    .map((other) => other.definition)
    .sort(() => Math.random() - 0.5)
    .slice(0, DISTRACTOR_COUNT);
  return {
    term: card.term,
    choices: [card.definition, ...distractors].sort(() => Math.random() - 0.5),
    answer: card.definition,
  };
}));
/** The question currently shown */
const currentQuestion = computed(() => questions.value[currentIndex.value]);
/** Progress fill percentage for the progress bar; 0% while the study set hasn't loaded yet */
const progressPercent = computed(() => {
  if (!questions.value.length) { return 0; }
  return Math.round(((currentIndex.value + 1) / questions.value.length) * 100);
});

// 3) =============== METHOD/FUNCTION  ===============
/** Move to the next question, or announce the round is complete and restart from the first
 * question once the last one is answered */
const advance = async () => {
  if (currentIndex.value >= questions.value.length - 1) {
    await dialog.showMessage(t('common.dialog.notice'), t('S0005.label.roundComplete'));
    currentIndex.value = 0;
  } else {
    currentIndex.value += 1;
  }
  selectedChoice.value = null;
  hintRevealed.value = false;
};

/** Highlight the picked choice; only a correct pick advances (after a short delay) — a wrong
 * pick just stays highlighted so the learner can try again on the same question */
const selectChoice = (choice) => {
  if (selectedChoice.value === currentQuestion.value?.answer) { return; }
  selectedChoice.value = choice;
  if (choice === currentQuestion.value?.answer) {
    setTimeout(advance, ADVANCE_DELAY_MS);
  }
};

/** Skip the current question without picking a choice (desktop-only "Don't know?" action) */
const skipQuestion = () => { advance(); };

/** Reveal the first letter of the answer as a hint (mobile-only "Hint" action) */
const showHint = () => { hintRevealed.value = true; };

/** Report the current question as problematic; no report backend yet, so just acknowledge it */
const reportQuestion = () => { dialog.showMessage(t('common.dialog.notice'), t('S0005.label.reportReceived')); };

// 4) =============== VUE JS LIFECYCLE ===============
// Keep the shared header's progress bar in sync with this screen's own progress
watchEffect(() => { studyHeader.percent = progressPercent.value; });
</script>

<template>
  <q-page class="learn-page">
    <div class="learn-page__progress-text">
      {{ t('S0005.label.progress', { current: currentIndex + 1, total: questions.length }) }}
    </div>

    <template v-if="currentQuestion">
      <div class="learn-question">
        <q-chip dense square color="lime-5" text-color="lime-1" class="learn-question__tag">
          {{ t('S0005.label.termTag') }}
        </q-chip>
        <div class="learn-question__text">{{ currentQuestion.term }}</div>
        <div v-if="hintRevealed" class="learn-question__hint">
          {{ t('S0005.label.hintText', { letter: currentQuestion.answer[0] }) }}
        </div>
      </div>

      <div class="learn-choices">
        <button v-for="(choice, index) in currentQuestion.choices" :key="choice" type="button" class="learn-choice"
          :class="{
            'learn-choice--correct': selectedChoice === choice && choice === currentQuestion.answer,
            'learn-choice--wrong': selectedChoice === choice && choice !== currentQuestion.answer,
          }" @click="selectChoice(choice)">
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
    </template>
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

  &--correct {
    border-color: $lime-1;
    background-color: $lime-5;
  }

  &--wrong {
    border-color: $negative;
    background-color: rgba($negative, 0.08);
    animation: learn-choice-shake 0.4s ease;
  }
}

@keyframes learn-choice-shake {

  0%,
  100% {
    transform: translateX(0);
  }

  20% {
    transform: translateX(-6px);
  }

  40% {
    transform: translateX(6px);
  }

  60% {
    transform: translateX(-4px);
  }

  80% {
    transform: translateX(4px);
  }
}

.learn-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
