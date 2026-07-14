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

  /**
   * Create (request.id null) or update (request.id set) a study set together with its cards,
   * uploading any newly-picked card images in the same request. Sent as multipart/form-data:
   * `data` (the JSON request) plus `files` (each card's new image, referenced from
   * `request.studyCards[].imageFileIndex`) — see server's AdminVideoVocabController#saveStudySet.
   */
  async saveStudySet(request, files = []) {
    const formData = new FormData();
    formData.append('data', JSON.stringify(request));
    files.forEach((file) => formData.append('files', file));

    // Override RestClient's default 'application/json' Content-Type: left as-is, axios would
    // serialize this FormData body back into JSON instead of sending it as multipart. Clearing
    // it here lets axios/the browser set 'multipart/form-data' with the correct boundary.
    const result = await this.post('/study-sets', formData, { headers: { 'Content-Type': undefined } });
    return result.payload.data;
  }
}

export default new VideoVocabService();
