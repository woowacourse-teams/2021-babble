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


    // TODO: Set 자료구조를 전부 List로 바꿨다. 엔티티간 id를 통해 동일성 비교를 하기 때문에 컬렉션에서 지워진 채로 1개만 삽입됐다.
    // TODO: AlternativeName은 Game이나 Tag에 종속적인 존재기 때문에, 이전에 포츈이 사용한 어노테이션 그걸로 바꿔보는걸 고려하자.
    @Transactional
    public TagResponse createTag(final TagCreateRequest request) {
        Tag tag = tagRepository.save(request.toEntity());

        return TagResponse.from(tag);
    }

    @Transactional
    public TagResponse updateTag(final Long tagId, final TagUpdateRequest request) {
        Tag tag = findById(tagId);
        tag.update(request.toEntity());

        return TagResponse.from(tag);
    }

    // TODO: 태그를 삭제할 때 대체 이름과 관계를 유지시킬 것인지? 아니면 그냥 관계도 끊어버릴 것인지?
    @Transactional
    public void deleteTag(final Long tagId) {
        Tag tag = findById(tagId);
        tag.delete();
    }
}
