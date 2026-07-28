<script setup>
import { ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import { ROUTER_NAME } from '@/helpers/const';
import dialog from '@/utilities/dialog';
import studyService from '@/services/study-service';
import homeService from '@/services/home-service';
import D0002_NewStudySet from '../D0002_NewStudySet.vue';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const router = useRouter();

// 2) =============== VARIABLE REF     ===============
/** Signed-in user's own study sets, fetched from GET /home/my-study-sets */
const mySets = ref([]);

// 3) =============== METHOD/FUNCTION  ===============
/** Fetch (or refetch) the current user's own study sets */
const loadMySets = async () => {
  const studySets = await homeService.getMyStudySets();
  mySets.value = studySets.map((studySet) => ({
    id: studySet.id,
    title: studySet.title,
    cardCount: studySet.studyCards?.length ?? 0,
  }));
};

/** Open a study set's flashcard mode */
const goToStudySet = (setId) => {
  router.push({ name: ROUTER_NAME.FLASHCARD, params: { setId } });
};

/** Load the full study set data and open the edit dialog, refreshing the list on save */
const onEditStudySet = async (setId) => {
  const fullStudySet = await studyService.getStudySet(setId);
  const saved = await dialog.showContent(t('D0002.label.editDialogTitle'), D0002_NewStudySet, {
    width: '760px',
    showHeader: true,
    params: { studySet: fullStudySet },
  });
  if (!saved) { return; }
  await loadMySets();
};

// 4) =============== VUE JS LIFECYCLE ===============
onMounted(loadMySets);
</script>

<template>
  <div class="my-study-sets tw:pb-6">
    <h2 class="my-study-sets__title">{{ t('S0011.label.title') }}</h2>

    <div v-if="mySets.length" class="study-set-row">
      <div v-for="set in mySets" :key="set.id" class="study-set-card" @click="goToStudySet(set.id)">
        <CBtn round dense flat icon="edit" color="grey-7" class="study-set-card__edit-btn"
          @click.stop="onEditStudySet(set.id)" />
        <q-icon name="menu_book" size="26px" color="lime-1" />
        <div class="study-set-card__title">{{ set.title }}</div>
        <div class="study-set-card__count">
          {{ t('S0002.label.studySetCardCount', { count: set.cardCount }) }}
        </div>
      </div>
    </div>

    <p v-else class="my-study-sets__empty">{{ t('S0011.label.empty') }}</p>
  </div>
</template>

<style lang="scss" scoped>
.my-study-sets {
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
  position: relative;
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

  // Quasar's own q-btn CSS sets position:relative outside of Tailwind's layer, taking
  // precedence over a tw:absolute utility class — set the position directly here instead.
  &__edit-btn {
    position: absolute !important;
    top: 8px;
    right: 8px;
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
