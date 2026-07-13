package vn.io.sontd.learning.server.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.io.sontd.learning.server.controller.BaseController;
import vn.io.sontd.learning.server.request.studyset.StudySetUpsertRequest;
import vn.io.sontd.learning.server.response.ResponseData;
import vn.io.sontd.learning.server.response.ResponseRoot;
import vn.io.sontd.learning.server.service.StudySetService;

/**
 * Admin endpoints for managing video-vocabulary content.
 */
@RestController
@RequestMapping("/api/v1/admin/video-vocab")
@RequiredArgsConstructor
public class AdminVideoVocabController extends BaseController {
    /** Substring used to find the "Daily Vocabulary" study sets by title (see {@code StudySetServiceImpl}, which auto-generates it). */
    private static final String DAILY_VOCABULARY_TITLE_KEYWORD = "Daily_English_Vocab";

    private final StudySetService studySetService;

    /**
     * Returns every study set whose title contains "Daily_English_Vocab", each with its study cards.
     */
    @GetMapping("/daily-vocabulary-sets")
    public ResponseRoot getDailyVocabularySets() {
        return success(new ResponseData<>(studySetService.findByTitleContaining(DAILY_VOCABULARY_TITLE_KEYWORD)));
    }

    /**
     * Creates or updates a study set together with its cards.
     * If {@code request.id} is present, the matching study set is updated and
     * its existing cards are deleted and replaced by {@code request.studyCards}.
     * If it's {@code null}, a new study set (and all of its cards) is created.
     */
    @PostMapping("/study-sets")
    public ResponseRoot saveStudySet(@RequestBody StudySetUpsertRequest request) {
        return success(new ResponseData<>(studySetService.saveStudySet(request)));
    }
}
