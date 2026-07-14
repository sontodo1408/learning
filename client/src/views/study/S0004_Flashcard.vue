<script setup>
import { ref, computed, inject, watchEffect, onMounted, onBeforeUnmount } from 'vue';
import { useI18n } from 'vue-i18n';
import { STUDY_HEADER_KEY, STUDY_SET_KEY } from '@/helpers/const';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const studyHeader = inject(STUDY_HEADER_KEY);
const studySet = inject(STUDY_SET_KEY);

// 2) =============== VARIABLE REF     ===============
/** Index of the card currently shown */
const currentIndex = ref(0);
/** Whether the current card is showing its back (definition) face */
const isFlipped = ref(false);

/** The card currently shown */
const currentCard = computed(() => studySet.studyCards[currentIndex.value]);
/** Whether the first card is showing (Prev is a no-op) */
const isFirstCard = computed(() => currentIndex.value === 0);
/** Whether the last card is showing (Next is a no-op) */
const isLastCard = computed(() => currentIndex.value === studySet.studyCards.length - 1);
/** Progress fill percentage for the progress bar; 0% while the study set hasn't loaded yet */
const progressPercent = computed(() => {
  if (!studySet.studyCards.length) { return 0; }
  return Math.round(((currentIndex.value + 1) / studySet.studyCards.length) * 100);
});

// 3) =============== METHOD/FUNCTION  ===============
/** Flip the current card between its term (front) and definition (back) faces */
const handleFlipCard = () => { isFlipped.value = !isFlipped.value; };

/** Move to the previous card, resetting it to the front face */
const handlePrev = () => {
  if (isFirstCard.value) { return; }
  currentIndex.value -= 1;
  isFlipped.value = false;
};

/** Move to the next card, resetting it to the front face */
const handleNext = () => {
  if (isLastCard.value) { return; }
  currentIndex.value += 1;
  isFlipped.value = false;
};

/** Space/ArrowUp/ArrowDown flip the card (desktop keyboard shortcut) */
const handleKeyDown = (event) => {
  if (event.code === 'Space' || event.code === 'ArrowUp' || event.code === 'ArrowDown') {
    event.preventDefault();
    handleFlipCard();
  }
};

// 4) =============== VUE JS LIFECYCLE ===============
onMounted(() => {
  window.addEventListener('keydown', handleKeyDown);
});

onBeforeUnmount(() => { window.removeEventListener('keydown', handleKeyDown); });

// Keep the shared header's progress bar in sync with this screen's own progress
watchEffect(() => { studyHeader.percent = progressPercent.value; });
</script>

<template>
  <q-page class="flashcard-page">
    <template v-if="currentCard">
      <div class="flashcard" @click="handleFlipCard">
        <div class="flashcard__inner" :class="{ 'flashcard__inner--flipped': isFlipped }">
          <div class="flashcard__face flashcard__face--front">
            <div class="flashcard__term">{{ currentCard.term }}</div>
            <div v-if="currentCard.pronounceTerm" class="flashcard__pronounce">{{ currentCard.pronounceTerm }}</div>
            <div class="flashcard__hint">{{ t('S0004.label.tapToReveal') }}</div>
          </div>
          <div class="flashcard__face flashcard__face--back">
            <div class="flashcard__definition">{{ currentCard.definition }}</div>
            <div v-if="currentCard.pronounceDef" class="flashcard__pronounce">{{ currentCard.pronounceDef }}</div>
          </div>
        </div>
      </div>

      <div class="flashcard-controls">
        <CBtn flat no-caps icon="chevron_left" :label="t('S0004.btn.prev')" :disable="isFirstCard" @click="handlePrev" />
        <CBtn unelevated no-caps :label="t('S0004.btn.flip')" class="flashcard-controls__flip" @click="handleFlipCard" />
        <CBtn flat no-caps icon-right="chevron_right" :label="t('S0004.btn.next')" :disable="isLastCard"
          @click="handleNext" />
      </div>

      <div class="flashcard-page__progress-text">
        {{ t('S0004.label.cardProgress', { current: currentIndex + 1, total: studySet.studyCards.length }) }}
      </div>
    </template>
  </q-page>
</template>

<style lang="scss" scoped>
.flashcard-page {
  &__progress-text {
    margin-top: 12px;
    text-align: center;
    font-size: 13px;
    color: rgba(#3a2a22, 0.6);
  }
}

.flashcard {
  perspective: 1200px;
  height: 280px;
  cursor: pointer;
  margin-bottom: 20px;

  &__inner {
    position: relative;
    width: 100%;
    height: 100%;
    transition: transform 0.6s;
    transform-style: preserve-3d;

    &--flipped {
      transform: rotateY(180deg);
    }
  }

  &__face {
    position: absolute;
    inset: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 24px;
    border-radius: 16px;
    text-align: center;
    backface-visibility: hidden;
    box-shadow: 0 8px 24px rgba($lime-1, 0.15);

    &--front {
      background-color: #fff;
    }

    &--back {
      background-color: $lime-4;
      transform: rotateY(180deg);
    }
  }

  &__term,
  &__definition {
    font-size: 28px;
    font-weight: 700;
    color: #3a2a22;
  }

  &__hint {
    margin-top: 12px;
    font-size: 13px;
    color: rgba(#3a2a22, 0.5);
  }

  &__pronounce {
    margin-top: 6px;
    font-size: 14px;
    font-style: italic;
    color: rgba(#3a2a22, 0.5);
  }
}

.flashcard-controls {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;

  &__flip {
    background-color: $lime-1;
    color: #fff;
    padding: 0 24px;
  }
}
</style>
