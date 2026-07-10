<script setup>
import { reactive, computed, provide } from 'vue';
import { useI18n } from 'vue-i18n';
import { useQuasar } from 'quasar';
import { useRoute, useRouter } from 'vue-router';
import { ROUTER_NAME, STUDY_HEADER_KEY } from '@/helpers/const';
import logoO from '@/assets/imgs/logo_o.png';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const $q = useQuasar();
const route = useRoute();
const router = useRouter();

// Desktop/mobile layout switches at this width (matches the design docs' `lg` breakpoint)
const DESKTOP_BREAKPOINT_PX = 1024;

/** Whether to render the sidebar (desktop) layout instead of the top+bottom bar (mobile) one */
const isDesktop = computed(() => $q.screen.width >= DESKTOP_BREAKPOINT_PX);

/** The 3 study modes, shared by the sidebar (desktop) and bottom tab bar (mobile) */
const modeTabs = computed(() => [
  { name: ROUTER_NAME.FLASHCARD, icon: 'style', label: t('study.nav.flashcards') },
  { name: ROUTER_NAME.LEARN, icon: 'school', label: t('study.nav.learn') },
  { name: ROUTER_NAME.TEST, icon: 'quiz', label: t('study.nav.test') },
]);

// 2) =============== VARIABLE REF     ===============
/** Study set title/tags/progress, rendered here but filled in by whichever screen is active */
const studyHeader = reactive({ title: '', tags: [], percent: 0 });
provide(STUDY_HEADER_KEY, studyHeader);

// 3) =============== METHOD/FUNCTION  ===============
/** Switch study mode, keeping the current study set in the route */
const goToTab = (tab) => {
  router.push({ name: tab.name, params: { setId: route.params.setId } });
};

// 4) =============== VUE JS LIFECYCLE ===============
</script>

<template>
  <q-layout view="hHh lpR fFf" class="study-layout">
    <!-- Mobile top bar: logo + notification/search (destinations not designed yet) -->
    <q-header v-if="!isDesktop" elevated class="bg-white text-grey-8">
      <q-toolbar>
        <img :src="logoO" alt="" class="study-layout__brand-logo" />
        <q-toolbar-title class="study-layout__brand">{{ t('common.app.name') }}</q-toolbar-title>
        <q-btn flat round icon="notifications" />
        <q-btn flat round icon="search" />
      </q-toolbar>
    </q-header>

    <q-page-container class="study-layout__content">
      <div class="study-layout__body">
        <!-- Desktop sidebar -->
        <div v-if="isDesktop" class="study-sidebar">
          <div class="study-sidebar__brand">
            <img :src="logoO" alt="" class="study-sidebar__brand-logo" />
            <span>{{ t('common.app.name') }}</span>
          </div>

          <CBtn unelevated no-caps icon="add" class="study-sidebar__new-set" :label="t('study.nav.newStudySet')" />

          <q-list class="tw:mt-4">
            <q-item v-for="tab in modeTabs" :key="tab.name" clickable
              :class="{ 'study-sidebar__item--active': route.name === tab.name }" class="study-sidebar__item"
              @click="goToTab(tab)">
              <q-item-section avatar>
                <q-icon :name="tab.icon" />
              </q-item-section>
              <q-item-section>{{ tab.label }}</q-item-section>
            </q-item>
          </q-list>

          <q-separator class="study-sidebar__separator tw:my-4!" />

          <q-list>
            <q-item clickable class="study-sidebar__item">
              <q-item-section avatar>
                <q-icon name="settings" />
              </q-item-section>
              <q-item-section>{{ t('study.nav.settings') }}</q-item-section>
            </q-item>
            <q-item clickable class="study-sidebar__item">
              <q-item-section avatar>
                <q-icon name="help_outline" />
              </q-item-section>
              <q-item-section>{{ t('study.nav.help') }}</q-item-section>
            </q-item>
          </q-list>
        </div>

        <div class="study-layout__main">
          <div class="study-header tw:pt-4">
            <h1 class="study-header__title">{{ studyHeader.title }}</h1>
            <div v-if="studyHeader.tags.length" class="study-header__tags">
              <q-chip v-for="tag in studyHeader.tags" :key="tag" dense square color="lime-5" text-color="lime-1">
                {{ tag }}
              </q-chip>
            </div>

            <div class="study-progress-bar">
              <div class="study-progress-bar__fill" :style="{ width: `${studyHeader.percent}%` }"></div>
            </div>
          </div>

          <div class="study-layout__page tw:pb-4">
            <router-view />
          </div>
        </div>
      </div>
    </q-page-container>

    <!-- Mobile bottom tab bar -->
    <q-footer v-if="!isDesktop" bordered class="bg-white">
      <q-tabs :model-value="route.name" active-color="lime-1" indicator-color="lime-1" class="text-grey-6">
        <q-tab v-for="tab in modeTabs" :key="tab.name" :name="tab.name" :icon="tab.icon" :label="tab.label"
          @click="goToTab(tab)" />
      </q-tabs>
    </q-footer>
  </q-layout>
</template>

<style lang="scss" scoped>
.study-layout__brand-logo {
  width: 24px;
  height: 24px;
  margin-right: 8px;
}

.study-layout__content {
  background-color: $lime-6;
  // Guarantee the colored background always covers at least the full viewport (border-box so
  // Quasar's own header/footer offset padding on this element is included, not added on top),
  // while still growing taller than that if a screen's real content is longer.
  min-height: 100vh;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}

.study-layout__body {
  display: flex;
  align-items: flex-start;
  justify-content: center;
  gap: 24px;
  // Stretch to fill .study-layout__content's height (the classic flex "sticky footer" sizing
  // trick) instead of only being as tall as its own content.
  flex: 1;
  min-height: 0;

  // Desktop: switch to a 2-col x 2-row grid so the sidebar can be placed on the
  // second row only, starting level with the page content instead of the header above it.
  @media (min-width: 1024px) {
    display: grid;
    grid-template-columns: 260px 33.333vw;
    grid-template-rows: auto 1fr;
    justify-content: center;
  }
}

.study-layout__main {
  flex: 1 1 auto;
  min-width: 0;

  // Desktop: shrink the screen content down to ~1/3 of the viewport width instead of
  // filling the rest of the sidebar row. Mobile keeps filling the available width.
  @media (min-width: 1024px) {
    // Dissolve this wrapper as a box so its children (header + page) become the
    // actual grid items of .study-layout__body, placed independently of the sidebar.
    display: contents;
  }
}

.study-layout__page {
  min-width: 0;

  @media (min-width: 1024px) {
    grid-column: 2;
    grid-row: 2;
  }

  // QPage always sets an inline min-height covering the full viewport (minus header/footer),
  // sized as if it were the only content — but study-header already takes up space above it,
  // so that min-height alone overflows the layout even when the actual content is short.
  :deep(.q-page) {
    min-height: 0 !important;
  }
}

.study-header {
  @media (min-width: 1024px) {
    grid-column: 2;
    grid-row: 1;
  }

  &__title {
    margin: 0 0 6px;
    font-size: 22px;
    line-height: 1.3;
    font-weight: 700;
    color: #3a2a22;
    // Always exactly 2 lines tall, regardless of how short/long the title is; overflow truncates with "..."
    height: calc(1.3em * 2);
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  &__tags {
    display: flex;
    gap: 6px;
    margin-bottom: 16px;
  }
}

.study-progress-bar {
  height: 8px;
  border-radius: 999px;
  background-color: $lime-5;
  overflow: hidden;

  &__fill {
    height: 100%;
    background-color: $lime-1;
    border-radius: 999px;
    transition: width 0.3s ease;
  }
}

.study-sidebar {
  flex: 0 0 260px;
  width: 260px;
  border-radius: 16px;
  background: linear-gradient(160deg, $lime-3 0%, $lime-4 100%);
  box-shadow: 0 8px 24px rgba($lime-1, 0.15);
  padding-bottom: 12px;

  @media (min-width: 1024px) {
    grid-column: 1;
    grid-row: 2;
  }

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
