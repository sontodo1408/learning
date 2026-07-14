<script setup>
import { ref, computed } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { ROUTER_NAME } from '@/helpers/const';
import { LOCALE_OPTIONS, setLocale } from '@/i18n';
import { useAuthStore } from '@/stores/auth-store';
import authService from '@/services/auth-service';

import logo_o from '@/assets/imgs/logo_o.png';

// 1) =============== INITIALIZATION   ===============
const route = useRoute();
const router = useRouter();
const { t, locale } = useI18n();
const authStore = useAuthStore();

/** The currently active locale's display option (name + flag) */
const currentLocaleOption = computed(() => LOCALE_OPTIONS.find((option) => option.code === locale.value));

/** Display name shown in the account menu: prefer the full name, fall back to the username */
const userDisplayName = computed(() => authStore.user?.fullName || authStore.user?.username || '');

/** First letter of the display name, used as the avatar's placeholder initial */
const userInitial = computed(() => userDisplayName.value.charAt(0).toUpperCase() || '?');

/** Sidebar navigation items; labels are recomputed whenever the active locale changes */
const navItem = computed(() => [
  { label: t('common.nav.dashboard'), icon: 'add', selected: [ROUTER_NAME.HOME], to: ROUTER_NAME.HOME },
  { label: t('common.nav.category'), icon: 'close', selected: [ROUTER_NAME.CATEGORY], to: ROUTER_NAME.CATEGORY },
  { label: t('common.nav.transaction'), icon: 'close', selected: [ROUTER_NAME.TRANSACTION_ADD], to: ROUTER_NAME.TRANSACTION_ADD },
  { label: t('common.nav.history'), icon: 'history', selected: [ROUTER_NAME.HISTORY], to: ROUTER_NAME.HISTORY },
  { label: t('common.nav.videoVocab'), icon: 'ondemand_video', selected: [ROUTER_NAME.VIDEO_VOCAB], to: ROUTER_NAME.VIDEO_VOCAB },
]);

// 2) =============== VARIABLE REF     ===============
const leftDrawerOpen = ref(true);

// 3) =============== METHOD/FUNCTION  ===============
const toggleLeftDrawer = () => { leftDrawerOpen.value = !leftDrawerOpen.value; };

const navItemOnClick = (item) => {
  router.push({ name: item.to });
};

/** Clear the session and send the user back to the login screen */
const handleLogout = () => {
  authService.logout();
  router.push({ name: ROUTER_NAME.LOGIN });
};

// 4) =============== VUE JS LIFECYCLE ===============
</script>

<template>
  <q-layout view="hHh Lpr lFf" class="layout-admin">
    <q-header elevated class="bg-white text-grey-8">
      <q-toolbar>
        <div class="tw:transition-all tw:duration-400" :class="leftDrawerOpen ? 'tw:rotate-180' : ''">
          <q-btn round flat icon="menu_open" @click="toggleLeftDrawer" />
        </div>

        <q-space />

        <!-- Language switcher: shows the currently active locale's flag + name, click to open the picker -->
        <q-btn flat no-caps class="tw:mr-2">
          <img :src="currentLocaleOption?.flag" alt="" class="tw:w-6 tw:mr-2 tw:rounded-xs" />
          {{ currentLocaleOption?.name }}
          <q-menu>
            <q-list>
              <q-item v-for="option in LOCALE_OPTIONS" :key="option.code" clickable v-close-popup
                @click="setLocale(option.code)">
                <q-item-section avatar class="tw:min-w-0 tw:pr-2">
                  <img :src="option.flag" alt="" class="tw:w-6 tw:rounded-xs" />
                </q-item-section>
                <q-item-section>{{ option.name }}</q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-btn>

        <!-- Account menu: shows the current user's name, logout below -->
        <q-btn round flat>
          <q-avatar size="32px" color="lime-1" text-color="white">{{ userInitial }}</q-avatar>
          <q-menu anchor="bottom right" self="top right">
            <q-list style="min-width: 180px">
              <q-item>
                <q-item-section>
                  <q-item-label>{{ userDisplayName }}</q-item-label>
                </q-item-section>
              </q-item>

              <q-separator />

              <q-item clickable v-close-popup @click="handleLogout">
                <q-item-section avatar class="tw:min-w-0 tw:pr-2">
                  <q-icon name="logout" color="negative" />
                </q-item-section>
                <q-item-section class="text-negative">{{ t('common.nav.logout') }}</q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-btn>
      </q-toolbar>
    </q-header>

    <q-drawer side="left" v-model="leftDrawerOpen" class="bg-lime-4">
      <q-list class="tw:mt-3">
        <div v-for="item in navItem" :key="item">
          <q-item class="nav-item" clickable :class="{ 'nav-selected': item.selected?.includes(route.name) }"
            @click="navItemOnClick(item)">
            <q-icon :name="item.icon" size="20px" class="tw:mr-5" color="lime-1" />
            <span class="nav-label">{{ item.label }}</span>
          </q-item>
        </div>
      </q-list>
    </q-drawer>

    <q-page-container class="tw:bg-lime-6">
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<style lang="scss" scoped>
// navigation
.nav-item {
  display: flex;
  align-items: center;

  .nav-label {
    font-size: 14px;
    color: $lime-1;
    font-weight: 600;
    transition: all .3s;
  }

  &:hover .nav-label {
    transform: translateX(10px);
  }
}

.nav-selected {
  background-color: $lime-6;
}
</style>