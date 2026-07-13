package vn.io.sontd.learning.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.io.sontd.learning.server.response.ResponseData;
import vn.io.sontd.learning.server.response.ResponseRoot;
import vn.io.sontd.learning.server.service.StudySetService;

/**
 * Study endpoints: fetching study sets and their cards.
 */
@RestController
@RequestMapping("/api/v1/study-sets")
@RequiredArgsConstructor
public class StudyController extends BaseController {
    private final StudySetService studySetService;

    /**
     * Returns a study set by id, together with its study cards.
     *
     * @param studySetId the study set's id
     */
    @GetMapping("/{studySetId}")
    public ResponseRoot getStudySet(@PathVariable Long studySetId) {
        return success(new ResponseData<>(studySetService.findById(studySetId)));
    }
}
