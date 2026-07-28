<script setup>
import { ref, computed } from 'vue';
import { useI18n } from 'vue-i18n';
import { useQuasar } from 'quasar';
import { useRoute, useRouter } from 'vue-router';
import { ROUTER_NAME } from '@/helpers/const';
import { useAuthStore } from '@/stores/auth-store';
import dialog from '@/utilities/dialog';
import D0002_NewStudySet from '../D0002_NewStudySet.vue';
import logoO from '@/assets/imgs/logo_o.png';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const $q = useQuasar();
const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

// Desktop/mobile layout switches at this width (matches the design docs' `lg` breakpoint)
const DESKTOP_BREAKPOINT_PX = 1024;

/** Whether to render the sidebar (desktop) layout instead of the bottom tab bar (mobile) one */
const isDesktop = computed(() => $q.screen.width >= DESKTOP_BREAKPOINT_PX);

/** Sidebar/tab-bar nav items */
const navItems = computed(() => [
  { key: 'home', icon: 'home', label: t('S0007.label.home'), routerName: ROUTER_NAME.USER_HOME },
  { key: 'mySets', icon: 'folder', label: t('S0007.label.mySets'), routerName: ROUTER_NAME.USER_MY_STUDY_SETS },
]);

// 2) =============== VARIABLE REF     ===============
/** Bumped after creating a study set to force the current child screen to remount and refetch its data */
const pageReloadKey = ref(0);

// 3) =============== METHOD/FUNCTION  ===============
/** Navigate to a nav item's screen; the router guard redirects to login first if it requires auth */
const goToNavItem = (item) => {
  if (item.routerName) {
    router.push({ name: item.routerName });
  }
};

/** "New study set" requires a signed-in user; signed-in users get the create-set dialog */
const onNewStudySet = async () => {
  if (!authStore.isLoggedIn) {
    router.push({ name: ROUTER_NAME.LOGIN, query: { redirect: route.fullPath } });
    return;
  }

  const studySet = await dialog.showContent(t('D0002.label.dialogTitle'), D0002_NewStudySet, {
    width: '760px',
    showHeader: true,
  });
  // Creating a study set keeps the user on the current page; just refetch its data
  if (studySet) { pageReloadKey.value += 1; }
};

// 4) =============== VUE JS LIFECYCLE ===============
</script>

<template>
  <q-page class="user-home-page">
    <div class="user-home-page__body tw:pt-4">
      <!-- Desktop sidebar -->
      <div v-if="isDesktop" class="user-home-sidebar">
        <div class="user-home-sidebar__brand">
          <img :src="logoO" alt="" class="user-home-sidebar__brand-logo" />
          <span>{{ t('common.app.name') }}</span>
        </div>

        <CBtn unelevated no-caps icon="add" class="user-home-sidebar__new-set" :label="t('S0007.label.newStudySet')"
          @click="onNewStudySet" />

        <q-list class="tw:mt-4">
          <q-item v-for="item in navItems" :key="item.key" clickable
            :class="{ 'user-home-sidebar__item--active': route.name === item.routerName }" class="user-home-sidebar__item"
            @click="goToNavItem(item)">
            <q-item-section avatar>
              <q-icon :name="item.icon" />
            </q-item-section>
            <q-item-section>{{ item.label }}</q-item-section>
          </q-item>
        </q-list>

        <q-separator class="user-home-sidebar__separator tw:my-4!" />

        <q-list>
          <q-item clickable class="user-home-sidebar__item">
            <q-item-section avatar>
              <q-icon name="settings" />
            </q-item-section>
            <q-item-section>{{ t('S0007.label.settings') }}</q-item-section>
          </q-item>
          <q-item clickable class="user-home-sidebar__item">
            <q-item-section avatar>
              <q-icon name="help_outline" />
            </q-item-section>
            <q-item-section>{{ t('S0007.label.help') }}</q-item-section>
          </q-item>
        </q-list>
      </div>

      <div class="user-home-page__main">
        <router-view :key="pageReloadKey" />
      </div>
    </div>

    <!-- Mobile bottom tab bar -->
    <q-footer v-if="!isDesktop" bordered class="bg-white">
      <q-tabs :model-value="route.name" active-color="lime-1" indicator-color="lime-1" class="text-grey-6">
        <q-tab v-for="item in navItems" :key="item.key" :name="item.routerName ?? item.key" :icon="item.icon"
          :label="item.label" @click="goToNavItem(item)" />
      </q-tabs>
    </q-footer>
  </q-page>
</template>

<style lang="scss" scoped>
.user-home-page {
  display: flex;
  flex-direction: column;

  &__body {
    display: flex;
    align-items: flex-start;
    justify-content: center;
    gap: 24px;
    // Stretch to fill .user-home-page's height (the classic flex "sticky footer" sizing
    // trick) instead of only being as tall as its own content.
    flex: 1;
    min-height: 0;
  }

  &__main {
    flex: 1 1 auto;
    min-width: 0;

    @media (min-width: 1024px) {
      max-width: 720px;
    }

    // QPage always sets an inline min-height covering the full viewport, sized as if this
    // were the only content — but the sidebar sits alongside it, so let child screens size
    // themselves naturally instead of stretching to fill the viewport.
    :deep(.q-page) {
      min-height: 0 !important;
    }
  }
}

.user-home-sidebar {
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
