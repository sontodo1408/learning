<script setup>
import { ref, computed, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import { useQuasar } from 'quasar';
import { useAuthStore } from '@/stores/auth-store';
import homeService from '@/services/home-service';
import logoO from '@/assets/imgs/logo_o.png';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const $q = useQuasar();
const authStore = useAuthStore();

// Desktop/mobile layout switches at this width (matches the design docs' `lg` breakpoint)
const DESKTOP_BREAKPOINT_PX = 1024;

/** Whether to render the sidebar (desktop) layout instead of the bottom tab bar (mobile) one */
const isDesktop = computed(() => $q.screen.width >= DESKTOP_BREAKPOINT_PX);

/** Placeholder nav items for the Home sidebar/tab bar (no destinations wired up yet) */
const navItems = computed(() => [
  { key: 'home', icon: 'home', label: t('S0007.label.home') },
  { key: 'mySets', icon: 'folder', label: t('S0007.label.mySets') },
]);

/** Mock recently-visited study sets — only shown to a signed-in user (no API/DB yet) */
const recentSets = [
  { title: 'Biology 101: Cell Structure', cardCount: 5 },
  { title: 'Advanced Biology: Cellular Respiration', cardCount: 4 },
  { title: 'Biology 101 Midterm', cardCount: 4 },
];

// 2) =============== VARIABLE REF     ===============
/** Currently highlighted nav item — purely visual for now, nothing to route to yet */
const activeKey = ref('home');

/** "Daily English Vocabulary" study sets, fetched from GET /home/newest-study-sets */
const dailyVocabSets = ref([]);

// 3) =============== METHOD/FUNCTION  ===============
// 4) =============== VUE JS LIFECYCLE ===============
onMounted(async () => {
  const studySets = await homeService.getNewestStudySets();
  dailyVocabSets.value = studySets.map((studySet) => ({
    id: studySet.id,
    title: studySet.title,
    cardCount: studySet.studyCards?.length ?? 0,
  }));
});
</script>

<template>
  <q-page class="home-page">
    <div class="home-page__body tw:pt-4">
      <!-- Desktop sidebar -->
      <div v-if="isDesktop" class="home-sidebar">
        <div class="home-sidebar__brand">
          <img :src="logoO" alt="" class="home-sidebar__brand-logo" />
          <span>{{ t('common.app.name') }}</span>
        </div>

        <CBtn unelevated no-caps icon="add" class="home-sidebar__new-set" :label="t('S0007.label.newStudySet')" />

        <q-list class="tw:mt-4">
          <q-item v-for="item in navItems" :key="item.key" clickable
            :class="{ 'home-sidebar__item--active': activeKey === item.key }" class="home-sidebar__item"
            @click="activeKey = item.key">
            <q-item-section avatar>
              <q-icon :name="item.icon" />
            </q-item-section>
            <q-item-section>{{ item.label }}</q-item-section>
          </q-item>
        </q-list>

        <q-separator class="home-sidebar__separator tw:my-4!" />

        <q-list>
          <q-item clickable class="home-sidebar__item">
            <q-item-section avatar>
              <q-icon name="settings" />
            </q-item-section>
            <q-item-section>{{ t('S0007.label.settings') }}</q-item-section>
          </q-item>
          <q-item clickable class="home-sidebar__item">
            <q-item-section avatar>
              <q-icon name="help_outline" />
            </q-item-section>
            <q-item-section>{{ t('S0007.label.help') }}</q-item-section>
          </q-item>
        </q-list>
      </div>

      <div class="home-page__main">
        <section class="home-section">
          <h2 class="home-section__title">{{ t('S0007.label.dailyVocabTitle') }}</h2>
          <p class="home-section__subtitle">{{ t('S0007.label.dailyVocabSubtitle') }}</p>

          <div class="study-set-row">
            <div v-for="set in dailyVocabSets" :key="set.id" class="study-set-card">
              <q-icon name="menu_book" size="26px" color="lime-1" />
              <div class="study-set-card__title">{{ set.title }}</div>
              <div class="study-set-card__count">
                {{ t('S0002.label.studySetCardCount', { count: set.cardCount }) }}
              </div>
            </div>
          </div>
        </section>

        <section v-if="authStore.isLoggedIn" class="home-section">
          <h2 class="home-section__title">{{ t('S0007.label.recentTitle') }}</h2>
          <p class="home-section__subtitle">{{ t('S0007.label.recentSubtitle') }}</p>

          <div class="study-set-row">
            <div v-for="set in recentSets" :key="set.title" class="study-set-card">
              <q-icon name="history" size="26px" color="lime-1" />
              <div class="study-set-card__title">{{ set.title }}</div>
              <div class="study-set-card__count">
                {{ t('S0002.label.studySetCardCount', { count: set.cardCount }) }}
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>

    <!-- Mobile bottom tab bar -->
    <q-footer v-if="!isDesktop" bordered class="bg-white">
      <q-tabs v-model="activeKey" active-color="lime-1" indicator-color="lime-1" class="text-grey-6">
        <q-tab v-for="item in navItems" :key="item.key" :name="item.key" :icon="item.icon" :label="item.label" />
      </q-tabs>
    </q-footer>
  </q-page>
</template>

<style lang="scss" scoped>
.home-page {
  display: flex;
  flex-direction: column;

  &__body {
    display: flex;
    align-items: flex-start;
    justify-content: center;
    gap: 24px;
    // Stretch to fill .home-page's height (the classic flex "sticky footer" sizing
    // trick) instead of only being as tall as its own content.
    flex: 1;
    min-height: 0;
  }

  &__main {
    flex: 1 1 auto;
    min-width: 0;
    display: flex;
    flex-direction: column;
    gap: 28px;
    padding-bottom: 24px;

    @media (min-width: 1024px) {
      max-width: 720px;
    }
  }
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

.home-sidebar {
  flex: 0 0 260px;
  width: 260px;
  border-radius: 16px;
  background: linear-gradient(160deg, $lime-3 0%, $lime-4 100%);
  box-shadow: 0 8px 24px rgba($lime-1, 0.15);
  padding-bottom: 12px;

  &__brand {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 20px 16px 8px;
    font-size: 15px;
    line-height: 1.25;
    font-weight: 900;
    color: #3a2a22;
  }

  &__brand-logo {
    flex: 0 0 auto;
    width: 24px;
    height: 24px;
  }

  &__new-set {
    margin: 4px 16px;
    width: calc(100% - 32px);
    background-color: $lime-1;
    color: #fff;
  }

  &__item {
    border-radius: 8px;
    margin: 0 8px;
    color: rgba(#3a2a22, 0.7);
  }

  &__item--active {
    background-color: #fff;
    color: $lime-1;
    font-weight: 600;
  }

  &__separator {
    background-color: rgba(#3a2a22, 0.15);
  }
}
</style>
