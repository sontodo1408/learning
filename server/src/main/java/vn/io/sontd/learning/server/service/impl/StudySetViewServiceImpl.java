package vn.io.sontd.learning.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.io.sontd.learning.server.config.security.UserDetailsImpl;
import vn.io.sontd.learning.server.entity.StudySetViewEntity;
import vn.io.sontd.learning.server.repository.StudySetViewRepository;
import vn.io.sontd.learning.server.service.BaseService;
import vn.io.sontd.learning.server.service.StudySetViewService;

import java.time.LocalDateTime;

/**
 * Default {@link StudySetViewService} implementation.
 */
@Service
@RequiredArgsConstructor
public class StudySetViewServiceImpl extends BaseService implements StudySetViewService {
    private final StudySetViewRepository studySetViewRepository;

    @Override
    public void recordView(Long studySetId) {
        Long userId = getAuth().map(UserDetailsImpl::getId).orElse(null);
        if (userId == null) {
            return;
        }

        StudySetViewEntity view = studySetViewRepository.findByUserIdAndStudySetId(userId, studySetId)
                .orElseGet(() -> {
                    StudySetViewEntity newView = new StudySetViewEntity();
                    newView.setUserId(userId);
                    newView.setStudySetId(studySetId);
                    return newView;
                });
        view.setViewedAt(LocalDateTime.now());
        studySetViewRepository.save(view);
    }
}
