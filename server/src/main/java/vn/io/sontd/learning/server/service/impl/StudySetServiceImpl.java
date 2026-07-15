package vn.io.sontd.learning.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.io.sontd.learning.server.constant.Constant;
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
import vn.io.sontd.learning.server.service.ImageStorageService;
import vn.io.sontd.learning.server.service.StudySetService;
import vn.io.sontd.learning.server.utils.CommonUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Default {@link StudySetService} implementation, backed by {@link StudySetRepository}
 * and {@link StudyCardRepository}.
 */
@Service
@RequiredArgsConstructor
public class StudySetServiceImpl implements StudySetService {
    private final StudySetRepository studySetRepository;
    private final StudyCardRepository studyCardRepository;
    private final ImageStorageService imageStorageService;

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
    public List<StudySetDTO> search(String keyword) {
        List<StudySetEntity> studySets = studySetRepository.searchByKeyword(keyword);
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

    @Override
    public List<StudySetDTO> findByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }

        // Fetch sets and cards in two bulk queries, then reassemble below.
        Map<Long, StudySetEntity> setById = studySetRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(StudySetEntity::getId, Function.identity()));

        Map<Long, List<StudyCardDTO>> cardsByStudySetId = studyCardRepository.findByStudySetIdInOrderByDisplayOrderAsc(ids)
                .stream()
                .map(this::toCardDTO)
                .collect(Collectors.groupingBy(StudyCardDTO::getStudySetId));

        // Preserve the caller's id order; skip ids that no longer resolve to a study set (e.g. deleted).
        return ids.stream()
                .map(setById::get)
                .filter(Objects::nonNull)
                .map(studySet -> toSetDTO(studySet, cardsByStudySetId.getOrDefault(studySet.getId(), List.of())))
                .toList();
    }

    @Override
    public List<StudySetDTO> findRecentlyCreated(int limit) {
        if (limit <= 0) {
            return List.of();
        }

        List<StudySetEntity> studySets = studySetRepository.findByUserIdOrderByCreatedAtDesc(
                Constant.VIDEO_VOCAB_USER_ID, PageRequest.of(0, limit));
        List<Long> studySetIds = studySets.stream().map(StudySetEntity::getId).toList();

        Map<Long, List<StudyCardDTO>> cardsByStudySetId = studyCardRepository.findByStudySetIdInOrderByDisplayOrderAsc(studySetIds)
                .stream()
                .map(this::toCardDTO)
                .collect(Collectors.groupingBy(StudyCardDTO::getStudySetId));

        return studySets.stream()
                .map(studySet -> toSetDTO(studySet, cardsByStudySetId.getOrDefault(studySet.getId(), List.of())))
                .toList();
    }

    /**
     * {@inheritDoc}
     * If updating an existing study set, its old cards are deleted before the new ones are
     * inserted, rather than matched/merged one by one; its {@code title}/{@code description}
     * are left untouched (whatever this study set was assigned when first created).
     * <p>
     * For a brand-new study set, {@code title} and {@code description} are always auto-generated
     * (as {@link Constant#DAILY_VOCAB_TITLE_PREFIX} and {@link Constant#DAILY_VOCAB_DESCRIPTION_PREFIX}
     * respectively, each followed by a sequential number), ignoring any title/description on the
     * request. That number is simply how many "Daily Vocab" study sets exist once this one is
     * inserted (e.g. the 11th such set becomes {@code #11}) — it tracks that count, not this study
     * set's own database id, so it needs an initial placeholder-title save (to create the row and
     * get it counted) followed by a second save that fills in the real title/description.
     * <p>
     * {@code userId} is likewise always {@link Constant#VIDEO_VOCAB_USER_ID}, ignoring any
     * user id on the request — every study set saved through this method belongs to the
     * "video vocab" feature, not a real user.
     */
    @Override
    @Transactional
    public StudySetDTO saveStudySet(StudySetUpsertRequest request, List<MultipartFile> files) {
        // Store any newly uploaded images first and point each card's imgUrl at them.
        resolveCardImages(request, files);

        StudySetEntity studySet;
        boolean isNew = request.getId() == null;
        if (isNew) {
            studySet = new StudySetEntity();
        } else {
            studySet = studySetRepository.findById(request.getId())
                    .orElseThrow(() -> new BusinessException(Message.STUDY_SET_NOT_FOUND));
            studyCardRepository.deleteByStudySetId(studySet.getId());
        }
        studySet.setUserId(Constant.VIDEO_VOCAB_USER_ID);
        studySet.setIsPublic(request.getIsPublic());

        if (isNew) {
            // Title is NOT NULL, so seed a placeholder to obtain the generated id and get this
            // row counted below, before filling in the real, sequentially-numbered title/description.
            studySet.setTitle(Constant.DAILY_VOCAB_TITLE_PREFIX);
            studySet = studySetRepository.save(studySet);

            long sequenceNumber = studySetRepository.countByTitleContaining(Constant.DAILY_VOCAB_TITLE_PREFIX);
            studySet.setTitle(Constant.DAILY_VOCAB_TITLE_PREFIX + sequenceNumber);
            studySet.setDescription(Constant.DAILY_VOCAB_DESCRIPTION_PREFIX + sequenceNumber);
            studySet = studySetRepository.save(studySet);
        }
        Long studySetId = studySet.getId();

        List<StudyCardEntity> newCards = request.getStudyCards() == null ? List.of()
                : request.getStudyCards().stream().map(cardRequest -> toNewCardEntity(studySetId, cardRequest)).toList();

        List<StudyCardDTO> savedCards = studyCardRepository.saveAll(newCards).stream()
                .sorted(Comparator.comparing(StudyCardEntity::getDisplayOrder))
                .map(this::toCardDTO)
                .toList();

        return toSetDTO(studySet, savedCards);
    }

    /**
     * For every card that references a newly uploaded file (via
     * {@link StudyCardUpsertRequest#getImageFileIndex()}), stores the file and rewrites
     * the card's {@code imgUrl} to the stored image's public URL. Cards without an index
     * keep their {@code imgUrl} untouched (an existing URL on update, or empty).
     * Runs inside the save transaction, so a later DB failure may leave an orphan file on disk.
     */
    private void resolveCardImages(StudySetUpsertRequest request, List<MultipartFile> files) {
        if (request.getStudyCards() == null) {
            return;
        }
        for (StudyCardUpsertRequest card : request.getStudyCards()) {
            Integer fileIndex = card.getImageFileIndex();
            if (fileIndex == null) {
                continue;
            }
            if (files == null || fileIndex < 0 || fileIndex >= files.size()) {
                throw new BusinessException(Message.IMAGE_FILE_INDEX_INVALID);
            }
            card.setImgUrl(imageStorageService.store(files.get(fileIndex), Constant.STUDY_CARD_IMAGE_SUBDIRECTORY));
        }
    }

    private StudyCardEntity toNewCardEntity(Long studySetId, StudyCardUpsertRequest request) {
        StudyCardEntity card = new StudyCardEntity();
        card.setStudySetId(studySetId);
        card.setTerm(request.getTerm());
        card.setDefinition(request.getDefinition());
        card.setPronounceTerm(request.getPronounceTerm());
        card.setPronounceDef(request.getPronounceDef());
        // Normalizes back to the canonical stored path, whether this is a fresh upload's
        // path (already bare, a no-op) or an existing image's public URL echoed back by the
        // client on an update — either way the DB always holds the version-independent form.
        card.setImgUrl(imageStorageService.toStoredPath(request.getImgUrl()));
        card.setDisplayOrder(CommonUtils.toInt(request.getDisplayOrder(), 0));
        return card;
    }

    private StudySetDTO toSetDTO(StudySetEntity studySet, List<StudyCardDTO> cards) {
        return new StudySetDTO(studySet.getId(), studySet.getUserId(), studySet.getTitle(), studySet.getDescription(),
                studySet.getIsPublic(), studySet.getCreatedAt(), studySet.getUpdatedAt(), cards);
    }

    private StudyCardDTO toCardDTO(StudyCardEntity card) {
        return new StudyCardDTO(card.getId(), card.getStudySetId(), card.getTerm(), card.getDefinition(),
                card.getPronounceTerm(), card.getPronounceDef(), card.getImgUrl(),
                card.getDisplayOrder(), card.getCreatedAt(), card.getUpdatedAt());
    }
}
