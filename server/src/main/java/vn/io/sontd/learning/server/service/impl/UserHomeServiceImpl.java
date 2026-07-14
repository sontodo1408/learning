package vn.io.sontd.learning.server.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.io.sontd.learning.server.dto.studyset.StudySetDTO;
import vn.io.sontd.learning.server.entity.StudySetViewEntity;
import vn.io.sontd.learning.server.entity.UserEntity;
import vn.io.sontd.learning.server.repository.StudySetViewRepository;
import vn.io.sontd.learning.server.repository.UserRepository;
import vn.io.sontd.learning.server.service.JwtService;
import vn.io.sontd.learning.server.service.StudySetService;
import vn.io.sontd.learning.server.service.UserHomeService;

import java.util.List;
import java.util.Optional;

/**
 * Default {@link UserHomeService} implementation.
 */
@Service
@RequiredArgsConstructor
public class UserHomeServiceImpl implements UserHomeService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final StudySetViewRepository studySetViewRepository;
    private final StudySetService studySetService;

    @Override
    public List<StudySetDTO> getRecentlyViewedStudySets(HttpServletRequest request) {
        String authToken = jwtService.getAuthToken(request);

        // Not logged in (no token) or a token we can't trust → empty list, never an error.
        if (StringUtils.isBlank(authToken) || !jwtService.validateJwtToken(authToken)) {
            return List.of();
        }

        // Resolve the token subject to a real user; a stale token for a removed user yields an empty list.
        String username = jwtService.getUsername(authToken);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return List.of();
        }

        // View records already come back most-recently-viewed first; keep that order when loading the sets.
        List<Long> studySetIds = studySetViewRepository.findByUserIdOrderByViewedAtDesc(user.get().getId())
                .stream()
                .map(StudySetViewEntity::getStudySetId)
                .toList();

        return studySetService.findByIds(studySetIds);
    }
}
