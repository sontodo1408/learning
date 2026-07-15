<script setup>
import { ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { ROUTER_NAME } from '@/helpers/const';
import homeService from '@/services/home-service';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const route = useRoute();
const router = useRouter();

// 2) =============== VARIABLE REF     ===============
/** Study sets matching the current keyword, fetched from GET /home/search */
const results = ref([]);
/** Whether a search request is in flight */
const isLoading = ref(false);

// 3) =============== METHOD/FUNCTION  ===============
/** Open a study set's flashcard mode */
const goToStudySet = (setId) => {
  router.push({ name: ROUTER_NAME.FLASHCARD, params: { setId } });
};

/** Re-run the search whenever the header's ?keyword= query changes */
const runSearch = async (keyword) => {
  if (!keyword) {
    results.value = [];
    return;
  }
  isLoading.value = true;
  try {
    const studySets = await homeService.search(keyword);
    results.value = studySets.map((studySet) => ({
      id: studySet.id,
      title: studySet.title,
      cardCount: studySet.studyCards?.length ?? 0,
    }));
  } finally {
    isLoading.value = false;
  }
};

// 4) =============== VUE JS LIFECYCLE ===============
watch(() => route.query.keyword, (keyword) => runSearch(keyword), { immediate: true });
</script>

<template>
  <div class="search-page tw:pb-6">
    <h2 class="search-page__title">
      {{ t('S0010.label.title', { keyword: route.query.keyword ?? '' }) }}
    </h2>

    <div v-if="results.length" class="study-set-row">
      <div v-for="set in results" :key="set.id" class="study-set-card" @click="goToStudySet(set.id)">
        <q-icon name="menu_book" size="26px" color="lime-1" />
        <div class="study-set-card__title">{{ set.title }}</div>
        <div class="study-set-card__count">
          {{ t('S0002.label.studySetCardCount', { count: set.cardCount }) }}
        </div>
      </div>
    </div>

    <p v-else-if="!isLoading" class="search-page__empty">{{ t('S0010.label.noResults') }}</p>
  </div>
</template>

<style lang="scss" scoped>
.search-page {
  &__title {
    margin: 0 0 14px;
    font-size: 19px;
    font-weight: 700;
    color: #3a2a22;
  }

  &__empty {
    margin: 0;
    font-size: 13px;
    color: rgba(#3a2a22, 0.55);
  }
}

.study-set-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 14px;
}

.study-set-card {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px;
  border-radius: 16px;
  background-color: #fff;
  box-shadow: 0 8px 24px rgba($lime-1, 0.12);
  cursor: pointer;
  transition: transform 0.15s ease;

  &:hover {
    transform: translateY(-2px);
  }

  &__title {
    font-size: 14px;
    font-weight: 700;
    color: #3a2a22;
    line-height: 1.3;
    // 2-line clamp so uneven titles keep every card the same height
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    overflow: hidden;
  }

  &__count {
    font-size: 12px;
    color: rgba(#3a2a22, 0.5);
  }
}
</style>
