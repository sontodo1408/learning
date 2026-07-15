<script setup>
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useQuasar } from 'quasar';
import { useRoute, useRouter } from 'vue-router';
import { ROUTER_NAME } from '@/helpers/const';
import { LOCALE_OPTIONS, setLocale } from '@/i18n';
import { useAuthStore } from '@/stores/auth-store';
import authService from '@/services/auth-service';
import logoO from '@/assets/imgs/logo_o.png';

// 1) =============== INITIALIZATION   ===============
const { t, locale } = useI18n();
const $q = useQuasar();
const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

// Desktop/mobile header switches at this width (matches the design docs' `lg` breakpoint)
const DESKTOP_BREAKPOINT_PX = 1024;

/** Whether to hide the mobile-only brand + notification/search bar */
const isDesktop = computed(() => $q.screen.width >= DESKTOP_BREAKPOINT_PX);

/** The currently active locale's display option (name + flag), same as AdminLayout's language switcher */
const currentLocaleOption = computed(() => LOCALE_OPTIONS.find((option) => option.code === locale.value));

/** Display name shown in the account menu: prefer the full name, fall back to the username */
const userDisplayName = computed(() => authStore.user?.fullName || authStore.user?.username || '');

/** First letter of the display name, used as the avatar's placeholder initial */
const userInitial = computed(() => userDisplayName.value.charAt(0).toUpperCase() || '?');

// 2) =============== VARIABLE REF     ===============
// Search box in the middle of the header, used to search study sets
const searchQuery = ref('');

// 3) =============== METHOD/FUNCTION  ===============
/** Clear the session and send the user back to the login screen */
const handleLogout = () => {
  authService.logout();
  router.push({ name: ROUTER_NAME.LOGIN });
};

/** Send a signed-out visitor to the login screen, carrying the current page along so login can return here */
const handleSignIn = () => {
  router.push({ name: ROUTER_NAME.LOGIN, query: { redirect: route.fullPath } });
};

/** Enter in the header search box: go to the search screen with the trimmed keyword */
const handleSearch = () => {
  const keyword = searchQuery.value.trim();
  if (!keyword) {
    return;
  }
  router.push({ name: ROUTER_NAME.USER_SEARCH, query: { keyword } });
};

/** Go back to the home screen ("/") when the header logo is clicked */
const handleLogoClick = () => {
  router.push({ name: ROUTER_NAME.USER_HOME });
};

// 4) =============== VUE JS LIFECYCLE ===============
</script>

<template>
  <q-layout view="hHh lpR fFf" class="user-layout">
    <!-- Top bar: mobile-only brand + notification/search (destinations not designed yet), plus the
    same language switcher and account/logout menu as AdminLayout's header, on both desktop and mobile -->
    <q-header elevated class="bg-white text-grey-8">
      <q-toolbar class="user-layout__toolbar">
        <img :src="logoO" alt="" class="user-layout__brand-logo" @click="handleLogoClick" />

        <q-space />

        <!-- Search box: absolutely centered on the toolbar so the language/account controls on the
        right (which are wider than the empty left side) don't pull it off-center -->
        <q-input v-model="searchQuery" dense outlined rounded bg-color="white"
          :placeholder="t('userLayout.label.searchPlaceholder')" class="user-layout__search" @keyup.enter="handleSearch">
          <template #prepend>
            <q-icon name="search" />
          </template>
        </q-input>

        <!-- Language switcher: shows the currently active locale's flag + name, click to open the picker -->
        <q-btn flat no-caps class="tw:mr-4!">
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
        <q-btn v-if="authStore.isLoggedIn" round flat>
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

        <!-- Not signed in: a lighter pill-shaped Sign in affordance instead of the avatar/menu -->
        <CBtn v-else outline rounded icon="person_outline" :label="t('common.nav.signIn')" class="user-layout__sign-in"
          @click="handleSignIn" />
      </q-toolbar>
    </q-header>

    <q-page-container class="tw:bg-lime-6">
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<style lang="scss" scoped>
.user-layout__brand-logo {
  width: 24px;
  height: 24px;
  margin-right: 8px;
  cursor: pointer;
}

.user-layout__sign-in {
  padding: 0 18px;
  font-weight: 600;
}

.user-layout__toolbar {
  position: relative;
}

.user-layout__search {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 420px;
}
</style>
