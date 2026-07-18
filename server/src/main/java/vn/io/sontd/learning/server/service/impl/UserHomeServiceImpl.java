package vn.io.sontd.learning.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.io.sontd.learning.server.config.security.UserDetailsImpl;
import vn.io.sontd.learning.server.dto.studyset.StudySetDTO;
import vn.io.sontd.learning.server.entity.StudySetViewEntity;
import vn.io.sontd.learning.server.repository.StudySetViewRepository;
import vn.io.sontd.learning.server.service.BaseService;
import vn.io.sontd.learning.server.service.StudySetService;
import vn.io.sontd.learning.server.service.UserHomeService;

import java.util.List;
import java.util.Optional;

/**
 * Default {@link UserHomeService} implementation.
 */
@Service
@RequiredArgsConstructor
public class UserHomeServiceImpl extends BaseService implements UserHomeService {
    private final StudySetViewRepository studySetViewRepository;
    private final StudySetService studySetService;

    @Override
    public List<StudySetDTO> getRecentlyViewedStudySets() {
        Optional<UserDetailsImpl> auth = getAuth();
        if (auth.isEmpty()) {
            return List.of();
        }

        // View records already come back most-recently-viewed first; keep that order when loading the sets.
        List<Long> studySetIds = studySetViewRepository.findByUserIdOrderByViewedAtDesc(auth.get().getId())
                .stream()
                .map(StudySetViewEntity::getStudySetId)
                .toList();

        return studySetService.findByIds(studySetIds);
    }

    @Override
    public List<StudySetDTO> getMyStudySets() {
        Optional<UserDetailsImpl> auth = getAuth();
        if (auth.isEmpty()) {
            return List.of();
        }

        return studySetService.findByUserId(auth.get().getId());
    }
}
