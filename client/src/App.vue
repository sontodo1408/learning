<script setup>
import { storeToRefs } from 'pinia';
import DialogFrame from '@/components/DialogFrame.vue';
import LoginPage from '@/views/S0003_Login.vue';
import { useAppStore } from '@/stores/app-store';
import { useAuthStore } from '@/stores/auth-store';

// 1) =============== INITIALIZATION   ===============
const isAuthProcess = false;
const { isLoading } = storeToRefs(useAppStore());
const { isLoggedIn } = storeToRefs(useAuthStore());

// 2) =============== VARIABLE REF     ===============
// 3) =============== METHOD/FUNCTION  ===============
// 4) =============== VUE JS LIFECYCLE ===============

</script>

<template>
  <q-layout v-if="isAuthProcess" view="hHh LpR fff">
    <q-page-container>
      <q-page class="column items-center">
        <div class="col row items-center">
          <q-circular-progress indeterminate size="xl" color="lime-1" />
        </div>
      </q-page>
    </q-page-container>
  </q-layout>

  <router-view v-else-if="isLoggedIn" v-slot="{ Component, route }">
    <transition :name="route.meta?.transition || 'fade'" mode="out-in">
      <component :is="Component"></component>
    </transition>
    <q-inner-loading :showing="isLoading" class="tw:z-100000 tw-fixed tw-top-0 tw:bottom-0">
      <q-spinner-pie size="5.5em" color="lime-1" />
    </q-inner-loading>

    <dialog-frame :key="route.fullPath" />
  </router-view>

  <q-layout v-else view="hHh lpR fFf">
    <q-page-container>
      <login-page />
    </q-page-container>
  </q-layout>

  <!-- <router-view v-else v-slot="{ Component, route }">
    <transition :name="route.meta?.transition || 'fade'" mode="out-in">
      <component :is="Component" :key="route.fullPath"></component>
    </transition>
    <q-inner-loading :showing="isLoading" class="tw:z-100000 tw-fixed tw-top-0 tw:bottom-0">
      <q-spinner-pie size="5.5em" color="lime-1" />
    </q-inner-loading>

    <dialog-frame :key="route.fullPath" />
  </router-view> -->

</template>
