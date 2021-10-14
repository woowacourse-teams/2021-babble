package gg.babble.babble.service;

import gg.babble.babble.domain.repository.TagRepository;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.dto.request.TagCreateRequest;
import gg.babble.babble.dto.request.TagRequest;
import gg.babble.babble.dto.request.TagUpdateRequest;
import gg.babble.babble.dto.response.TagResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    public TagService(final TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> findAllById(final List<TagRequest> tagRequests) {
        return tagRequests.stream()
            .map(tagRequest -> findById(tagRequest.getId()))
            .collect(Collectors.toList());
    }

    private Tag findById(final Long id) {
        return tagRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%d]는 존재하지 않는 태그 ID입니다.", id)));
    }

    public List<TagResponse> findAll() {
        List<Tag> tags = tagRepository.findByDeletedFalse();

        return tags.stream()
            .map(TagResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public TagResponse createTag(final TagCreateRequest request) {
        Tag tag = tagRepository.save(request.toEntity());

        return TagResponse.from(tag);
    }

    @Transactional
    public TagResponse updateTag(final Long tagId, final TagUpdateRequest request) {
        Tag tag = findById(tagId);
        tag.update(request.getName(), request.getAlternativeNames());

        return TagResponse.from(findById(tagId));
    }

    @Transactional
    public void deleteTag(final Long tagId) {
        Tag tag = findById(tagId);
        tag.delete();
    }
}
