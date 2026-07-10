<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { useI18n } from 'vue-i18n';
import Sortable from 'sortablejs';
import { v4 as uuid } from 'uuid';
import alarmRingSound from '@/assets/sounds/alarm_ring.wav';
import phoneBackground from '@/assets/imgs/video_learn_vocab_background.png';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();

/** Create a fresh, empty vocab card. `uid` is the client-side key; `id` is reserved for the server-side id (set later) */
const newCard = () => ({ uid: uuid(), id: null, term: '', definition: '', image: null, imageFile: null });

// Durations for the playback sequence: ringing alarm -> masked question w/ countdown -> revealed answer
const ALARM_DURATION_MS = 3000;
const COUNTDOWN_SECONDS = 5;
const ANSWER_DURATION_MS = 3000;

// 2) =============== VARIABLE REF     ===============
/** List of vocab cards being edited */
const cards = ref([newCard()]);
/** Wrapper element of the card list, used to attach SortableJS */
const listRef = ref(null);
/** Map of hidden file input elements keyed by card uid, used to open the file picker */
const fileInputs = {};
/** SortableJS instance, kept to destroy it on unmount */
let sortable = null;

/** Current playback stage: 'idle' | 'ringing' | 'question' | 'answer' */
const playStage = ref('idle');
/** Index of the card currently being played */
const playIndex = ref(0);
/** Seconds left to display during the countdown ticks */
const countdown = ref(COUNTDOWN_SECONDS);

// 3) =============== METHOD/FUNCTION  ===============
/** Append a new empty card to the end of the list */
const addCard = () => { cards.value.push(newCard()); };

/** Remove a card by uid and revoke its image object URL, if any */
const removeCard = (uid) => {
  const card = cards.value.find((c) => c.uid === uid);
  if (card?.image) { URL.revokeObjectURL(card.image); }
  cards.value = cards.value.filter((c) => c.uid !== uid);
};

/** Store the hidden file input ref for the given card uid */
const setFileInputRef = (el, uid) => { if (el) { fileInputs[uid] = el; } };

/** Open the file picker of the given card */
const triggerFileInput = (uid) => { fileInputs[uid]?.click(); };

/** Handle image selection: keep the raw File for upload and an object URL to preview it */
const onImageChange = (event, card) => {
  const file = event.target.files?.[0];
  if (!file) { return; }
  if (card.image) { URL.revokeObjectURL(card.image); }
  card.image = URL.createObjectURL(file);
  card.imageFile = file;
  event.target.value = '';
};

/** Clear the card's image, revoke its object URL and drop the stored File */
const removeImage = (card) => {
  if (card.image) { URL.revokeObjectURL(card.image); }
  card.image = null;
  card.imageFile = null;
};

/** Log all card data to the console for easy inspection */
const logCards = () => { console.log(cards.value); };

// --- Playback: ringing alarm -> masked question w/ countdown -> revealed answer -> next card ---
/** The card currently shown by the playback sequence */
const currentPlayCard = computed(() => cards.value[playIndex.value] ?? null);

/** Shared AudioContext, created lazily since it needs a user gesture to start */
let audioCtx = null;
/** The alarm ring `<audio>` element, created lazily and looped while ringing */
let alarmAudio = null;
let countdownIntervalId = null;
let stageTimeoutId = null;
let speakTimeoutId = null;

/** Mask a word: keep its first letter (uppercased) and replace the rest with underscores */
const maskWord = (text) => {
  if (!text) { return ''; }
  return text
    .split(' ')
    .map((word) => (word.length <= 1 ? word : word[0].toUpperCase() + '_'.repeat(word.length - 1)))
    .join(' ');
};

/** Lazily create (or reuse) the shared AudioContext used for all playback sounds */
const getAudioContext = () => {
  if (!audioCtx) { audioCtx = new (window.AudioContext || window.webkitAudioContext)(); }
  // Browsers can create the context in a "suspended" state even from a click handler; resume it so sound is audible
  if (audioCtx.state === 'suspended') { audioCtx.resume(); }
  return audioCtx;
};

/** Play a single short beep at the given frequency/waveform, fading out over `durationMs` */
const playTone = (frequency, durationMs, type = 'sine', volume = 0.2) => {
  const ctx = getAudioContext();
  const oscillator = ctx.createOscillator();
  const gain = ctx.createGain();
  oscillator.type = type;
  oscillator.frequency.value = frequency;
  oscillator.connect(gain);
  gain.connect(ctx.destination);
  gain.gain.setValueAtTime(volume, ctx.currentTime);
  gain.gain.exponentialRampToValueAtTime(0.0001, ctx.currentTime + durationMs / 1000);
  oscillator.start();
  oscillator.stop(ctx.currentTime + durationMs / 1000);
};

/** Lazily create (or reuse) the looping alarm ring `<audio>` element */
const getAlarmAudio = () => {
  if (!alarmAudio) {
    alarmAudio = new Audio(alarmRingSound);
    alarmAudio.loop = true;
  }
  return alarmAudio;
};

/** Play the real alarm ring sound file ("reng reng"), looping until stopped */
const startAlarmSound = () => {
  const audio = getAlarmAudio();
  audio.currentTime = 0;
  audio.play();
};

/** Stop the alarm ring sound, if currently playing */
const stopAlarmSound = () => {
  alarmAudio?.pause();
  if (alarmAudio) { alarmAudio.currentTime = 0; }
};

/** Play a single clock-tick sound, used once per countdown second */
const playTickSound = () => { playTone(1000, 80); };

/** Play a bright "ting" sound, used the moment the answer is revealed */
const playDingSound = () => { playTone(1500, 400, 'sine', 0.3); };

/** Read the given English text aloud via the browser's speech synthesis */
const speakEnglish = (text) => {
  if (!text || !window.speechSynthesis) { return; }
  const utterance = new SpeechSynthesisUtterance(text);
  utterance.lang = 'en-US';
  window.speechSynthesis.speak(utterance);
};

/** Clear every pending timer/interval used by the playback sequence */
const clearPlayTimers = () => {
  clearTimeout(stageTimeoutId);
  clearTimeout(speakTimeoutId);
  clearInterval(countdownIntervalId);
  stopAlarmSound();
  window.speechSynthesis?.cancel();
};

/** Run the masked question w/ countdown -> revealed answer sequence for the current card, then advance */
const playCurrentCard = () => {
  playStage.value = 'question';
  countdown.value = COUNTDOWN_SECONDS;
  let ticksLeft = COUNTDOWN_SECONDS;

  countdownIntervalId = setInterval(() => {
    playTickSound();
    ticksLeft -= 1;
    countdown.value = ticksLeft;

    if (ticksLeft <= 0) {
      clearInterval(countdownIntervalId);
      playStage.value = 'answer';
      playDingSound();
      speakTimeoutId = setTimeout(() => speakEnglish(currentPlayCard.value?.definition), 400);
      stageTimeoutId = setTimeout(() => {
        playIndex.value += 1;
        if (playIndex.value < cards.value.length) {
          playCurrentCard();
        } else {
          playStage.value = 'idle';
        }
      }, ANSWER_DURATION_MS);
    }
  }, 1000);
};

/** Start playback: ring the alarm once, then go through every card's question/answer sequence */
const startPlayback = () => {
  if (!cards.value.length || playStage.value !== 'idle') { return; }
  playIndex.value = 0;
  playStage.value = 'ringing';
  startAlarmSound();

  stageTimeoutId = setTimeout(() => {
    stopAlarmSound();
    playCurrentCard();
  }, ALARM_DURATION_MS);
};

/** Stop playback and clear any pending timer/sound */
const stopPlayback = () => {
  clearPlayTimers();
  playStage.value = 'idle';
};

// 4) =============== VUE JS LIFECYCLE ===============
onMounted(() => {
  // Enable drag-to-reorder on the card list, draggable only via the drag-handle icon
  sortable = Sortable.create(listRef.value, {
    handle: '.drag-handle',
    animation: 150,
    onEnd(evt) {
      // SortableJS already moved the DOM node; sync the data order to match
      const moved = cards.value.splice(evt.oldIndex, 1)[0];
      cards.value.splice(evt.newIndex, 0, moved);
    },
  });
});

onBeforeUnmount(() => {
  // Destroy the SortableJS instance to avoid leaking it
  sortable?.destroy();
  // Stop any pending playback timers/sounds and release the AudioContext
  clearPlayTimers();
  audioCtx?.close();
});
</script>

<template>
  <q-page class="flex">
    <div class="tw:flex-1 tw:pr-20 tw:h-full tw:overflow-y-auto tw:p-4">
      <div ref="listRef">
        <div v-for="card in cards" :key="card.uid"
          class="tw:flex tw:items-center tw:border tw:border-gray-200 tw:rounded-lg tw:bg-white tw:mb-3">
          <q-icon name="drag_indicator" size="20px" class="drag-handle tw:cursor-move tw:mx-2 tw:text-gray-400" />

          <div class="tw:flex-1 tw:p-4">
            <q-input v-model="card.term" :placeholder="t('S0002.label.termPlaceholder')" borderless dense />
          </div>

          <div class="tw:flex-1 tw:p-4 tw:border-l tw:border-gray-200">
            <q-input v-model="card.definition" :placeholder="t('S0002.label.definitionPlaceholder')" borderless dense />
          </div>

          <div class="image-picker tw:relative tw:w-16 tw:h-16 tw:my-3 tw:mx-2 tw:shrink-0">
            <input type="file" accept="image/*" aria-label="Card image" class="tw:hidden"
              :ref="(el) => setFileInputRef(el, card.uid)" @change="onImageChange($event, card)" />

            <div
              class="tw:w-16 tw:h-16 tw:rounded-md tw:border tw:border-dashed tw:border-gray-300 tw:flex tw:items-center tw:justify-center tw:overflow-hidden tw:cursor-pointer tw:bg-gray-50"
              @click="triggerFileInput(card.uid)">
              <img v-if="card.image" :src="card.image" alt="Card" class="tw:w-full tw:h-full tw:object-cover" />
              <q-icon v-else name="add_photo_alternate" size="24px" class="tw:text-gray-400" />
            </div>

            <CBtn v-if="card.image" round dense flat icon="close" size="8px" class="remove-image-btn tw:bg-white tw:shadow"
              @click.stop="removeImage(card)" />
          </div>

          <div class="tw:flex tw:items-center tw:px-3">
            <CBtn round dense flat icon="delete_outline" color="grey-7" @click="removeCard(card.uid)" />
          </div>
        </div>
      </div>

      <div class="tw:flex tw:gap-2">
        <c-btn flat no-caps icon="add" :label="t('S0002.btn.addCard')" color="primary" @click="addCard" />
        <c-btn flat no-caps icon="bug_report" :label="t('S0002.btn.logData')" color="grey-7" @click="logCards" />
        <c-btn v-if="playStage === 'idle'" flat no-caps icon="play_circle" :label="t('S0002.btn.play')"
          color="secondary" @click="startPlayback" />
        <c-btn v-else flat no-caps icon="stop_circle" :label="t('S0002.btn.stop')" color="negative"
          @click="stopPlayback" />
      </div>
    </div>

    <div class="tw:flex tw:px-10 tw:items-center tw:justify-center">
      <div class="phone-screen" :style="{ backgroundImage: `url(${phoneBackground})` }">
        <div class="play-stage tw:flex tw:items-center tw:justify-center tw:p-4">
          <div class="content-card tw:flex tw:flex-col tw:items-center tw:justify-center tw:gap-4 tw:p-4">
            <template v-if="playStage === 'ringing'">
              <q-icon name="alarm" size="64px" color="white" class="alarm-icon" />
              <div class="tw:text-white tw:mt-4 tw:text-lg">{{ t('S0002.label.ringing') }}</div>
            </template>

            <template v-else-if="playStage === 'question' || playStage === 'answer'">
              <div class="tw:text-white tw:text-3xl tw:font-semibold tw:text-center">{{ currentPlayCard?.term }}</div>

              <img v-if="currentPlayCard?.image" :src="currentPlayCard.image" alt=""
                class="tw:max-w-full tw:max-h-40 tw:rounded-md tw:object-cover" />

              <div class="tw:text-white tw:text-2xl tw:tracking-widest tw:text-center">
                {{ playStage === 'answer' ? currentPlayCard?.definition : maskWord(currentPlayCard?.definition) }}
              </div>
            </template>
          </div>
        </div>

        <div v-if="playStage === 'question'" :key="countdown" class="countdown-number">{{ countdown }}</div>
      </div>
    </div>
  </q-page>
</template>

<style lang="scss" scoped>
// Quasar's own q-btn CSS sets position:relative outside of Tailwind's layer, so it
// always wins over the tw:absolute utility class — force the position here instead.
.image-picker .remove-image-btn {
  position: absolute !important;
  top: -8px;
  right: -8px;
}

.phone-screen {
  position: relative;
  width: 320px;
  aspect-ratio: 9 / 16;
  max-height: 85vh;
  background-color: #000;
  background-size: cover;
  background-position: center;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.35);
  overflow: hidden;
}

.content-card {
  width: 85%;
  min-height: 220px;
  background-color: rgba(0, 0, 0, 0.45);
  backdrop-filter: blur(4px);
  border-radius: 12px;
}

.play-stage {
  position: absolute;
  inset: 0;
}

@keyframes alarm-shake {

  0%,
  100% {
    transform: rotate(0deg);
  }

  20% {
    transform: rotate(-15deg);
  }

  40% {
    transform: rotate(15deg);
  }

  60% {
    transform: rotate(-10deg);
  }

  80% {
    transform: rotate(10deg);
  }
}

.alarm-icon {
  animation: alarm-shake 0.3s infinite;
}

.countdown-number {
  position: absolute;
  top: 16px;
  right: 16px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 10px;
  background-color: #333;
  color: #fff;
  font-size: 1.5rem;
  font-weight: 700;
  perspective: 300px;
  animation: countdown-flip 0.5s ease-out;
}

@keyframes countdown-flip {
  0% {
    transform: scale(1.8) rotateX(90deg);
    opacity: 0;
  }

  60% {
    transform: scale(1.15) rotateX(0deg);
    opacity: 1;
  }

  100% {
    transform: scale(1) rotateX(0deg);
    opacity: 1;
  }
}
</style>
