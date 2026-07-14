<script setup>
import { ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { DIALOG_BTN } from '@/helpers/const';
import videoVocabService from '@/services/video-vocab-service';

// 1) =============== INITIALIZATION   ===============
const emit = defineEmits(['done']);
const { t } = useI18n();

// 2) =============== VARIABLE REF     ===============
/** Existing study sets to pick from, each with its studyCards embedded */
const studySets = ref([]);
/** Whether the list is still being fetched */
const loading = ref(true);

// 3) =============== METHOD/FUNCTION  ===============
/** Resolve the dialog with the picked study set */
const selectStudySet = (studySet) => { emit('done', studySet); };

/** Comma-joined preview of a study set's card terms, shown so the user can recognize the set by its words */
const termsPreview = (studySet) => studySet.studyCards?.map((card) => card.term).filter(Boolean).join(', ') ?? '';

// 4) =============== VUE JS LIFECYCLE ===============
onMounted(async () => {
  try {
    studySets.value = await videoVocabService.getDailyVocabularySets();
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="study-set-picker">
    <q-list separator class="study-set-picker__list tw:p-4">
      <q-item v-if="loading">
        <q-item-section class="tw:items-center">
          <q-spinner color="primary" size="24px" />
        </q-item-section>
      </q-item>

      <q-item v-else-if="!studySets.length">
        <q-item-section class="tw:text-gray-400">{{ t('S0002.label.noStudySets') }}</q-item-section>
      </q-item>

      <q-item v-for="studySet in studySets" v-else :key="studySet.id" clickable v-ripple
        @click="selectStudySet(studySet)">
        <q-item-section>
          <q-item-label>{{ studySet.title }}</q-item-label>
          <q-item-label caption>
            {{ t('S0002.label.studySetCardCount', { count: studySet.studyCards?.length ?? 0 }) }}
          </q-item-label>
          <q-item-label caption class="tw:truncate">{{ termsPreview(studySet) }}</q-item-label>
        </q-item-section>
      </q-item>
    </q-list>

    <q-card-section class="dialog-btn-container">
      <CBtn outline :label="DIALOG_BTN.CLOSE" v-close-popup class="tw:min-w-27!" />
    </q-card-section>
  </div>
</template>

<style lang="scss" scoped>
.study-set-picker {
  &__list {
    max-height: 60vh;
    overflow-y: auto;
  }
}
</style>
