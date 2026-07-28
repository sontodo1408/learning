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

  /** Fetch the current user's own study sets */
  async getMyStudySets() {
    const result = await this.get('/my-study-sets');
    return result.payload.data;
  }

  /**
   * Create (request.id null) or update (request.id set) a study set together with its study
   * cards for the current user, uploading any card images in the same request. Sent as
   * multipart/form-data: `data` (the JSON request) plus `files` (each card's image, referenced
   * from `request.studyCards[].imageFileIndex`) — mirrors the admin video-vocab save endpoint's
   * contract; the backend tells create/update apart by whether `id` is present.
   */
  async saveStudySet(request, files = []) {
    const formData = new FormData();
    formData.append('data', JSON.stringify(request));
    files.forEach((file) => formData.append('files', file));

    // Override RestClient's default 'application/json' Content-Type so axios/the browser set
    // 'multipart/form-data' with the correct boundary instead of serializing the FormData as JSON.
    const result = await this.post('/my-study-sets', formData, { headers: { 'Content-Type': undefined } });
    return result.payload.data;
  }
}

export default new HomeService();
