import { defineStore } from 'pinia';

export const useAppStore = defineStore('appStore', {
  state: () => ({
    // Number of API requests currently in flight, not just the last call's state — so when
    // several requests overlap, the indicator only clears once all of them have finished.
    loadingCount: 0,
  }),
  getters: {
    isLoading: (state) => state.loadingCount > 0,
  },
  actions: {
    setLoading(value) {
      this.loadingCount = Math.max(0, this.loadingCount + (value ? 1 : -1));
    },
  },
});
