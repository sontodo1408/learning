import BaseService from '@/services/base-service';

/** Public user-home endpoints (mirrors server's /api/v1/home/*) */
class HomeService extends BaseService {
  constructor() {
    super('/home');
  }

  /** Fetch the most recently created study sets, each with its study cards, most recent first */
  async getNewestStudySets(limit) {
    const result = await this.get('/newest-study-sets', limit != null ? { limit } : undefined);
    return result.payload.data;
  }

  /** Fetch the current user's most recently viewed study sets, most recent first (empty when logged out) */
  async getRecentStudySets() {
    const result = await this.get('/recent-study-sets');
    return result.payload.data;
  }

  /** Search study sets whose title/description/cards match the given keyword */
  async search(keyword) {
    const result = await this.get('/search', { keyword });
    return result.payload.data;
  }
}

export default new HomeService();
