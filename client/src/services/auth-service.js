import BaseService from '@/services/base-service';
import { useAuthStore } from '@/stores/auth-store';

/** Auth endpoints: login and current-session check (mirrors server's /api/auth/*) */
class AuthService extends BaseService {
  constructor() {
    super('/auth');
  }

  /** Log in with username/password, persist the token + profile in authStore, and return the profile */
  async login(username, password) {
    const result = await this.post('/login', { username, password });
    useAuthStore().signIn(result.payload.token, result.payload);
    return result.payload;
  }

  /** Log in with a Google ID token (obtained client-side via Google Sign-In), persist the token + profile in authStore, and return the profile */
  async googleLogin(idToken) {
    const result = await this.post('/google-login', { idToken });
    useAuthStore().signIn(result.payload.token, result.payload);
    return result.payload;
  }

  /** Re-fetch the current user's profile; also validates the stored JWT is still accepted */
  async checkLogin() {
    const result = await this.get('/check-login');
    useAuthStore().setUser(result.payload);
    return result.payload;
  }

  /** Clear the stored session */
  logout() {
    useAuthStore().signOut();
  }
}

export default new AuthService();
