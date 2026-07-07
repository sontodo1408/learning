import { defineStore } from 'pinia';

export const useDialogStore = defineStore('dialog', {
  state: () => ({
    dialog: [],
  }),
  getters: {
    findById: (state) => (id) => state.dialog.find((x) => x.id === id),
  },
  actions: {
    show(data) {
      this.dialog.push(data);
    },

    hide(id) {
      const index = this.dialog.findIndex((item) => item.id === id);
      if (index > -1) {
        this.dialog.splice(index, 1);
      }
    },
  },
});
