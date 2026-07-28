<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { useI18n } from 'vue-i18n';
import Sortable from 'sortablejs';
import { v4 as uuid } from 'uuid';
import dialog from '@/utilities/dialog';
import { DIALOG_BTN, WORD_TYPE, WORD_TYPE_LABEL_KEY } from '@/helpers/const';
import { useAuthStore } from '@/stores/auth-store';
import { resolveImageUrl } from '@/utilities/common';
import homeService from '@/services/home-service';

// 1) =============== INITIALIZATION   ===============
/** When set, the dialog edits this existing study set instead of creating a new one */
const props = defineProps({ studySet: { type: Object, default: null } });
const emit = defineEmits(['done']);
const { t } = useI18n();
const authStore = useAuthStore();

/** Create a fresh, empty study card. `uid` is the client-side list key; `id` is reserved for the server-side id */
const newCard = () => ({
  uid: uuid(), id: null, term: '', pronounceDef: '', definition: '', wordType: null, imgUrl: null, image: null, imageFile: null,
});

/** Map a server study card into this editor's card shape */
const cardFromStudyCard = (studyCard) => ({
  uid: uuid(),
  id: studyCard.id,
  term: studyCard.term ?? '',
  definition: studyCard.definition ?? '',
  pronounceDef: studyCard.pronounceDef ?? '',
  wordType: studyCard.wordType ?? null,
  // Raw server-relative path, kept as-is to send back unchanged if the image isn't replaced
  imgUrl: studyCard.imgUrl ?? null,
  // Absolute URL for display/preview
  image: resolveImageUrl(studyCard.imgUrl),
  imageFile: null,
});

/** Options for the word type ("loại từ") select, labeled via i18n */
const wordTypeOptions = computed(() => Object.values(WORD_TYPE).map((value) => ({
  value,
  label: t(WORD_TYPE_LABEL_KEY[value]),
})));

// 2) =============== VARIABLE REF     ===============
/** Study set's title */
const title = ref(props.studySet?.title ?? '');
/** Study set's description */
const description = ref(props.studySet?.description ?? '');
/** Whether the study set is visible to other users */
const isPublic = ref(props.studySet?.isPublic ?? true);
/** Cards being edited; the existing set's cards when editing, otherwise a single empty row */
const cards = ref(
  props.studySet?.studyCards?.length ? props.studySet.studyCards.map(cardFromStudyCard) : [newCard()],
);
/** Whether a save request is in flight */
const saving = ref(false);
/** Wrapper element of the card list, used to attach SortableJS */
const listRef = ref(null);
/** Map of hidden file input elements keyed by card uid, used to open the file picker */
const fileInputs = {};
/** SortableJS instance, kept to destroy it on unmount */
let sortable = null;

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
  card.imgUrl = null;
  event.target.value = '';
};

/** Clear the card's image, revoke its object URL and drop the stored File */
const removeImage = (card) => {
  if (card.image) { URL.revokeObjectURL(card.image); }
  card.image = null;
  card.imageFile = null;
  card.imgUrl = null;
};

/** A title and at least one fully-filled card are required to save */
const canSave = computed(() => (
  title.value.trim().length > 0
  && cards.value.some((card) => card.term.trim() && card.definition.trim())
));

/** Build the multipart save payload from the current editor state, dropping incomplete rows */
const buildSaveRequest = () => {
  const files = [];
  const studyCards = cards.value
    .filter((card) => card.term.trim() && card.definition.trim())
    .map((card, index) => {
      let imageFileIndex = null;
      if (card.imageFile) {
        imageFileIndex = files.length;
        files.push(card.imageFile);
      }
      return {
        term: card.term.trim(),
        definition: card.definition.trim(),
        pronounceTerm: '',
        pronounceDef: card.pronounceDef,
        wordType: card.wordType,
        imgUrl: card.imgUrl,
        displayOrder: index + 1,
        imageFileIndex,
      };
    });

  const data = {
    id: props.studySet?.id ?? null,
    title: title.value.trim(),
    description: description.value.trim(),
    isPublic: isPublic.value,
    userId: props.studySet?.userId ?? authStore.user?.id,
    studyCards,
  };

  return { data, files };
};

/** Save the study set together with its cards, then resolve the dialog with the saved result */
const saveStudySet = async () => {
  saving.value = true;
  try {
    const { data, files } = buildSaveRequest();
    const saved = await homeService.saveStudySet(data, files);
    dialog.showMessage(t('common.dialog.notice'), t('D0002.label.saveSuccess'));
    emit('done', saved);
  } finally {
    saving.value = false;
  }
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

onBeforeUnmount(() => { sortable?.destroy(); });
</script>

<template>
  <div class="new-study-set">
    <q-input v-model="title" outlined dense :label="t('D0002.label.titlePlaceholder')" class="tw:mb-3" />
    <q-input v-model="description" outlined dense autogrow :label="t('D0002.label.descriptionPlaceholder')"
      class="tw:mb-3" />

    <q-toggle v-model="isPublic" color="lime-1" :label="t('D0002.label.isPublic')" class="tw:mb-4" />

    <div class="new-study-set__cards-label tw:mb-2">{{ t('D0002.label.cardsSection') }}</div>

    <div class="new-study-set__cards" ref="listRef">
      <div v-for="card in cards" :key="card.uid" class="card-row tw:flex tw:items-stretch tw:border tw:border-gray-200
        tw:rounded-lg tw:bg-white tw:mb-3">
        <q-icon name="drag_indicator" size="20px" class="drag-handle tw:self-center tw:cursor-move tw:mx-2 tw:text-gray-400" />

        <div class="tw:flex-1 tw:min-w-0">
          <div class="tw:flex tw:items-center">
            <div class="tw:flex-1 tw:p-3">
              <q-input v-model="card.term" :placeholder="t('D0002.label.termPlaceholder')" borderless dense />
            </div>

            <div class="tw:flex-1 tw:p-3 tw:border-l tw:border-gray-200">
              <q-input v-model="card.definition" :placeholder="t('D0002.label.definitionPlaceholder')" borderless dense />
            </div>
          </div>

          <q-separator />

          <div class="tw:flex tw:items-center">
            <div class="tw:flex-1 tw:p-3">
              <q-input v-model="card.pronounceDef" :placeholder="t('D0002.label.pronounceDefPlaceholder')" borderless
                dense />
            </div>

            <div class="tw:flex-1 tw:p-3 tw:border-l tw:border-gray-200">
              <CSelect v-model="card.wordType" :outlined="false" :options="wordTypeOptions" :option-label="['label']"
                option-value="value" display-value="label" :label="t('D0002.label.wordTypePlaceholder')" />
            </div>
          </div>
        </div>

        <div class="image-picker tw:w-28 tw:shrink-0 tw:p-2 tw:border-l tw:border-gray-200">
          <input type="file" accept="image/*" aria-label="Card image" class="tw:hidden"
            :ref="(el) => setFileInputRef(el, card.uid)" @change="onImageChange($event, card)" />

          <div class="image-picker__frame tw:relative tw:w-full tw:h-full">
            <div
              class="tw:w-full tw:h-full tw:rounded-md tw:border tw:border-dashed tw:border-gray-300 tw:flex tw:items-center tw:justify-center tw:overflow-hidden tw:cursor-pointer tw:bg-gray-50 tw:transition-colors hover:tw:bg-gray-100"
              @click="triggerFileInput(card.uid)">
              <img v-if="card.image" :src="card.image" alt="Card" class="tw:w-full tw:h-full tw:object-cover" />
              <q-icon v-else name="add_photo_alternate" size="28px" class="tw:text-gray-400" />
            </div>

            <CBtn v-if="card.image" round dense flat icon="close" size="8px"
              class="remove-image-btn tw:bg-white tw:shadow" @click.stop="removeImage(card)" />
          </div>
        </div>

        <div class="tw:flex tw:items-center tw:px-2 tw:border-l tw:border-gray-200">
          <CBtn round dense flat icon="delete_outline" color="grey-7" @click="removeCard(card.uid)" />
        </div>
      </div>
    </div>

    <CBtn flat no-caps icon="add" :label="t('D0002.btn.addCard')" color="primary" class="tw:mb-2" @click="addCard" />

    <q-card-section class="dialog-btn-container tw:px-0">
      <CBtn outline :label="DIALOG_BTN.CLOSE" v-close-popup class="tw:min-w-27!" />
      <CBtn unelevated no-caps icon="save" :label="t('D0002.btn.save')" :loading="saving" :disable="!canSave"
        @click="saveStudySet" />
    </q-card-section>
  </div>
</template>

<style lang="scss" scoped>
.new-study-set {
  padding: 20px;

  &__cards-label {
    font-size: 13px;
    font-weight: 600;
    color: rgba(#3a2a22, 0.6);
  }

  &__cards {
    max-height: 45vh;
    overflow-y: auto;
    overflow-x: hidden;
  }
}

.card-row:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
}

// Quasar's own q-btn CSS sets position:relative outside of Tailwind's layer, so it
// always wins over the tw:absolute utility class — force the position here instead.
.image-picker__frame .remove-image-btn {
  position: absolute !important;
  top: -8px;
  right: -8px;
}
</style>
