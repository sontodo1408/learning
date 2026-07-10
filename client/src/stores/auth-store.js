import { defineStore } from 'pinia';

// TODO: token/user only live in memory for now (lost on page refresh); add persistence later
export const useAuthStore = defineStore('auth', {
  state: () => ({
    /** JWT issued by the server on login */
    token: null,
    /** Profile of the currently logged-in user (mirrors server's LoginResponse, minus the token) */
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
    },

    /** Update just the user profile, e.g. after re-fetching it via check-login */
    setUser(user) {
      this.user = user;
    },

    /** Clear the session, e.g. on logout or when the server rejects the token (401) */
    signOut() {
      this.token = null;
      this.user = null;
    },
  },
});
