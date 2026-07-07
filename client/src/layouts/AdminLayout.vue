<script setup>
import { ref } from "vue";
import logo from "@/assets/imgs/logo_o.png";

// 1) =============== INITIALIZATION   ===============
const menuItems = [{ label: "Trang chủ", icon: "home", to: "/" }];

// 2) =============== VARIABLE REF     ===============
const leftDrawerOpen = ref(true);

// 3) =============== METHOD/FUNCTION  ===============
const toggleLeftDrawer = () => {
  leftDrawerOpen.value = !leftDrawerOpen.value;
};

// 4) =============== VUE JS LIFECYCLE ===============
</script>

<template>
  <q-layout view="hHh Lpr lFf">
    <q-header bordered class="bg-white text-dark">
      <q-toolbar>
        <q-btn flat dense round icon="menu" aria-label="Menu" @click="toggleLeftDrawer" />
        <img :src="logo" alt="logo" class="tw-h-8 tw-mx-2" />
        <q-toolbar-title>Admin</q-toolbar-title>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawerOpen" show-if-above bordered>
      <q-list>
        <q-item v-for="item in menuItems" :key="item.label" :to="item.to" clickable v-ripple>
          <q-item-section avatar>
            <q-icon :name="item.icon" />
          </q-item-section>
          <q-item-section>{{ item.label }}</q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>