<script setup>
import { storeToRefs } from 'pinia';
import DialogFrame from '@/components/DialogFrame.vue';
import { useAppStore } from '@/stores/app-store';

// 1) =============== INITIALIZATION   ===============
const { isLoading } = storeToRefs(useAppStore());

// 2) =============== VARIABLE REF     ===============
// 3) =============== METHOD/FUNCTION  ===============
// 4) =============== VUE JS LIFECYCLE ===============

</script>

<template>
  <router-view v-slot="{ Component, route }">
    <transition :name="route.meta?.transition || 'fade'" mode="out-in">
      <component :is="Component"></component>
    </transition>
    <q-inner-loading :showing="isLoading" class="tw:z-100000 tw-fixed tw-top-0 tw:bottom-0">
      <q-spinner-pie size="5.5em" color="lime-1" />
    </q-inner-loading>

    <dialog-frame :key="route.fullPath" />
  </router-view>
</template>
