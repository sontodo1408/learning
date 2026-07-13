package vn.io.sontd.learning.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.io.sontd.learning.server.constant.Message;
import vn.io.sontd.learning.server.dto.studyset.StudyCardDTO;
import vn.io.sontd.learning.server.dto.studyset.StudySetDTO;
import vn.io.sontd.learning.server.entity.StudyCardEntity;
import vn.io.sontd.learning.server.entity.StudySetEntity;
import vn.io.sontd.learning.server.exception.BusinessException;
import vn.io.sontd.learning.server.repository.StudyCardRepository;
import vn.io.sontd.learning.server.repository.StudySetRepository;
import vn.io.sontd.learning.server.request.studyset.StudyCardUpsertRequest;
import vn.io.sontd.learning.server.request.studyset.StudySetUpsertRequest;
import vn.io.sontd.learning.server.service.StudySetService;
import vn.io.sontd.learning.server.utils.CommonUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Default {@link StudySetService} implementation, backed by {@link StudySetRepository}
 * and {@link StudyCardRepository}.
 */
@Service
@RequiredArgsConstructor
public class StudySetServiceImpl implements StudySetService {
    /** Prefix for the auto-generated study set title, followed by the study set's id. */
    private static final String DAILY_VOCAB_TITLE_PREFIX = "Daily_English_Vocab#";

    private final StudySetRepository studySetRepository;
    private final StudyCardRepository studyCardRepository;

    @Override
    public List<StudySetDTO> findByTitleContaining(String title) {
        List<StudySetEntity> studySets = studySetRepository.findByTitleContainingOrderByTitleAsc(title);
        List<Long> studySetIds = studySets.stream().map(StudySetEntity::getId).toList();

        Map<Long, List<StudyCardDTO>> cardsByStudySetId = studyCardRepository.findByStudySetIdInOrderByDisplayOrderAsc(studySetIds)
                .stream()
                .map(this::toCardDTO)
                .collect(Collectors.groupingBy(StudyCardDTO::getStudySetId));

        return studySets.stream()
                .map(studySet -> toSetDTO(studySet, cardsByStudySetId.getOrDefault(studySet.getId(), List.of())))
                .toList();
    }

    @Override
    public StudySetDTO findById(Long studySetId) {
        StudySetEntity studySet = studySetRepository.findById(studySetId)
                .orElseThrow(() -> new BusinessException(Message.STUDY_SET_NOT_FOUND));

        List<StudyCardDTO> cards = studyCardRepository.findByStudySetIdInOrderByDisplayOrderAsc(List.of(studySetId))
                .stream()
                .map(this::toCardDTO)
                .toList();

        return toSetDTO(studySet, cards);
    }

    /**
     * {@inheritDoc}
     * If updating an existing study set, its old cards are deleted before
     * the new ones are inserted, rather than matched/merged one by one.
     * {@code title} is always auto-generated as {@value DAILY_VOCAB_TITLE_PREFIX}
     * followed by the study set's id; for a brand-new study set this means an
     * initial save (to obtain the generated id) followed by a second save
     * that fills in the real title.
     */
    @Override
    @Transactional
    public StudySetDTO saveStudySet(StudySetUpsertRequest request) {
        StudySetEntity studySet;
        boolean isNew = request.getId() == null;
        if (isNew) {
            studySet = new StudySetEntity();
        } else {
            studySet = studySetRepository.findById(request.getId())
                    .orElseThrow(() -> new BusinessException(Message.STUDY_SET_NOT_FOUND));
            studyCardRepository.deleteByStudySetId(studySet.getId());
        }
        studySet.setUserId(request.getUserId());
        studySet.setDescription(request.getDescription());
        studySet.setIsPublic(request.getIsPublic());

        if (isNew) {
            studySet.setTitle(DAILY_VOCAB_TITLE_PREFIX);
            studySet = studySetRepository.save(studySet);
        }
        studySet.setTitle(DAILY_VOCAB_TITLE_PREFIX + studySet.getId());
        studySet = studySetRepository.save(studySet);
        Long studySetId = studySet.getId();

        List<StudyCardEntity> newCards = request.getStudyCards() == null ? List.of()
                : request.getStudyCards().stream().map(cardRequest -> toNewCardEntity(studySetId, cardRequest)).toList();

        List<StudyCardDTO> savedCards = studyCardRepository.saveAll(newCards).stream()
                .sorted(Comparator.comparing(StudyCardEntity::getDisplayOrder))
                .map(this::toCardDTO)
                .toList();

        return toSetDTO(studySet, savedCards);
    }

    private StudyCardEntity toNewCardEntity(Long studySetId, StudyCardUpsertRequest request) {
        StudyCardEntity card = new StudyCardEntity();
        card.setStudySetId(studySetId);
        card.setTerm(request.getTerm());
        card.setDefinition(request.getDefinition());
        card.setPronounceTerm(request.getPronounceTerm());
        card.setPronounceDef(request.getPronounceDef());
        card.setImgUrl(request.getImgUrl());
        card.setDisplayOrder(CommonUtils.toInt(request.getDisplayOrder(), 0));
        return card;
    }

    private StudySetDTO toSetDTO(StudySetEntity studySet, List<StudyCardDTO> cards) {
        return new StudySetDTO(studySet.getId(), studySet.getUserId(), studySet.getTitle(), studySet.getDescription(),
                studySet.getIsPublic(), studySet.getCreatedAt(), studySet.getUpdatedAt(), cards);
    }

    private StudyCardDTO toCardDTO(StudyCardEntity card) {
        return new StudyCardDTO(card.getId(), card.getStudySetId(), card.getTerm(), card.getDefinition(),
                card.getPronounceTerm(), card.getPronounceDef(), card.getImgUrl(), card.getDisplayOrder(),
                card.getCreatedAt(), card.getUpdatedAt());
    }
}
