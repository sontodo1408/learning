<script setup>
import { useDialogStore } from '@/stores/dialog-store';
import dialog from '@/utilities/dialog';

// 1) =============== INITIALIZATION   ===============
const dialogStore = useDialogStore();
const info = dialogStore.dialog;

// 2) =============== VARIABLE REF     ===============

// 3) =============== METHOD/FUNCTION  ===============
// 現在ポップアップを閉じるイベント
const cancel = (id) => {
  dialog.hide(id);
};

// フッターに該当するイベントをキャッチする
const btnClick = (id, btn) => {
  dialog.hide(id, btn);
};

// 4) =============== VUE JS LIFECYCLE ===============
</script>
<template>
  <q-dialog v-for="item in info" :key="item.id" :model-value="item.show" @hide="cancel(item.id)" persistent>
    <q-card :style="{ width: item.config.width, height: item.config.height }" class="c-dialog">
      <div class="dialog-header" v-if="item.config.showHeader">
        <div>{{ item.title }}</div>
      </div>
      <div v-if="item.config.isComponent">
        <component :is="item.content" v-bind="item.config.params" @done="(v) => dialog.hide(item.id, v)" />
      </div>
      <q-card-section v-if="!item.config.isComponent" class="tw:bg-lime-6">
        <div class="tw:whitespace-break-spaces">{{ item.content }}</div>
      </q-card-section>
      <q-card-section v-if="!item.config.isComponent" class="dialog-btn-container">
        <div v-for="(btn, index) in item.config.buttons" :key="btn">
          <c-btn :outline="index === 0" :label="btn" @click="btnClick(item.id, btn)" class="tw:min-w-27!" />
        </div>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>
