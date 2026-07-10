import { defineStore } from 'pinia';

// Key used to persist the JWT across page refreshes
const STORAGE_KEY = 'auth_token';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    /** JWT issued by the server on login; restored from localStorage so it survives a page refresh */
    token: localStorage.getItem(STORAGE_KEY),
    /** Profile of the currently logged-in user (mirrors server's LoginResponse, minus the token); re-fetched via checkLogin after a refresh */
    user: null,
  }),
  getters: {
    /** Whether a user is currently signed in */
    isLoggedIn: (state) => !!state.token,
  },
  actions: {
    /** Store the JWT and the logged-in user's profile after a successful login */
    signIn(token, user) {
      this.token = token;
      this.user = user;
      localStorage.setItem(STORAGE_KEY, token);
    },

    /** Update just the user profile, e.g. after re-fetching it via check-login */
    setUser(user) {
      this.user = user;
    },

    /** Clear the session, e.g. on logout or when the server rejects the token (401) */
    signOut() {
      this.token = null;
      this.user = null;
      localStorage.removeItem(STORAGE_KEY);
    },
  },
});
