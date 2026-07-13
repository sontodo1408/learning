import BaseService from '@/services/base-service';

/** Admin video-vocab endpoints (mirrors server's /api/v1/admin/video-vocab/*) */
class VideoVocabService extends BaseService {
  constructor() {
    super('/admin/video-vocab');
  }

  /** Fetch the "Daily Vocaburary" study sets, each with its study cards embedded */
  async getDailyVocabularySets() {
    const result = await this.get('/daily-vocabulary-sets');
    return result.payload.data;
  }

  /** Create (request.id null) or update (request.id set) a study set together with its cards */
  async saveStudySet(request) {
    const result = await this.post('/study-sets', request);
    return result.payload.data;
  }
}

export default new VideoVocabService();
