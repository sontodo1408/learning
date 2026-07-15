package vn.io.sontd.learning.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.io.sontd.learning.server.dto.studyset.StudySetDTO;
import vn.io.sontd.learning.server.response.ResponseData;
import vn.io.sontd.learning.server.response.ResponseRoot;
import vn.io.sontd.learning.server.service.StudySetService;
import vn.io.sontd.learning.server.service.StudySetViewService;

/**
 * Study endpoints: fetching study sets and their cards.
 */
@RestController
@RequestMapping("/api/v1/study-sets")
@RequiredArgsConstructor
public class StudyController extends BaseController {
    private final StudySetService studySetService;
    private final StudySetViewService studySetViewService;

    /**
     * Returns a study set by id, together with its study cards.
     * If the caller is logged in (valid bearer token), also records this as a view of
     * the study set via {@link StudySetViewService}.
     *
     * @param studySetId the study set's id
     */
    @GetMapping("/{studySetId}")
    public ResponseRoot getStudySet(@PathVariable Long studySetId) {
        StudySetDTO studySet = studySetService.findById(studySetId);
        studySetViewService.recordView(studySetId);
        return success(new ResponseData<>(studySet));
    }
}
