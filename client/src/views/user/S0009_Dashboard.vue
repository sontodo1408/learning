<script setup>
import { ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import { ROUTER_NAME } from '@/helpers/const';
import { useAuthStore } from '@/stores/auth-store';
import homeService from '@/services/home-service';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const router = useRouter();
const authStore = useAuthStore();

// 2) =============== VARIABLE REF     ===============
/** "Daily English Vocabulary" study sets, fetched from GET /home/newest-study-sets */
const dailyVocabSets = ref([]);

/** Signed-in user's recently-visited study sets, fetched from GET /home/recent-study-sets */
const recentSets = ref([]);

// 3) =============== METHOD/FUNCTION  ===============
/** Open a study set's flashcard mode */
const goToStudySet = (setId) => {
  router.push({ name: ROUTER_NAME.FLASHCARD, params: { setId } });
};

// 4) =============== VUE JS LIFECYCLE ===============
onMounted(async () => {
  const studySets = await homeService.getNewestStudySets();
  dailyVocabSets.value = studySets.map((studySet) => ({
    id: studySet.id,
    title: studySet.title,
    cardCount: studySet.studyCards?.length ?? 0,
  }));

  if (authStore.isLoggedIn) {
    const recentStudySets = await homeService.getRecentStudySets();
    recentSets.value = recentStudySets.map((studySet) => ({
      id: studySet.id,
      title: studySet.title,
      cardCount: studySet.studyCards?.length ?? 0,
    }));
  }
});
</script>

<template>
  <div class="dashboard tw:pb-6">
    <section class="home-section">
      <h2 class="home-section__title">{{ t('S0009.label.dailyVocabTitle') }}</h2>
      <p class="home-section__subtitle">{{ t('S0009.label.dailyVocabSubtitle') }}</p>

      <div class="study-set-row">
        <div v-for="set in dailyVocabSets" :key="set.id" class="study-set-card" @click="goToStudySet(set.id)">
          <q-icon name="menu_book" size="26px" color="lime-1" />
          <div class="study-set-card__title">{{ set.title }}</div>
          <div class="study-set-card__count">
            {{ t('S0002.label.studySetCardCount', { count: set.cardCount }) }}
          </div>
        </div>
      </div>
    </section>

    <section v-if="authStore.isLoggedIn" class="home-section">
      <h2 class="home-section__title">{{ t('S0009.label.recentTitle') }}</h2>
      <p class="home-section__subtitle">{{ t('S0009.label.recentSubtitle') }}</p>

      <div class="study-set-row">
        <div v-for="set in recentSets" :key="set.id" class="study-set-card" @click="goToStudySet(set.id)">
          <q-icon name="history" size="26px" color="lime-1" />
          <div class="study-set-card__title">{{ set.title }}</div>
          <div class="study-set-card__count">
            {{ t('S0002.label.studySetCardCount', { count: set.cardCount }) }}
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style lang="scss" scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.home-section {
  &__title {
    margin: 0 0 2px;
    font-size: 19px;
    font-weight: 700;
    color: #3a2a22;
  }

  &__subtitle {
    margin: 0 0 14px;
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
    // 2-line clamp so uneven mock titles keep every card the same height
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
