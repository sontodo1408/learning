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
}

export default new HomeService();
