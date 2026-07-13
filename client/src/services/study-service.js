import BaseService from '@/services/base-service';

/** Public study endpoints (mirrors server's /api/v1/study-sets/*) */
class StudyService extends BaseService {
  constructor() {
    super('/study-sets');
  }

  /** Fetch a study set together with its study cards by id */
  async getStudySet(studySetId) {
    const result = await this.get(`/${studySetId}`);
    return result.payload.data;
  }
}

export default new StudyService();
