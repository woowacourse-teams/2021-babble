package gg.babble.babble.service;

import gg.babble.babble.domain.Tag;
import gg.babble.babble.domain.repository.TagRepository;
import gg.babble.babble.dto.TagRequest;
import gg.babble.babble.dto.TagResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag findById(final String name) {
        return tagRepository.findById(name)
                .orElseThrow(() -> new BabbleNotFoundException("존재하지 않는 태그입니다."));
    }

    public List<Tag> findById(final List<TagRequest> tagRequests) {
        return tagRequests.stream()
                .map(tagRequest -> findById(tagRequest.getName()))
                .collect(Collectors.toList());
    }

    public List<TagResponse> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());
    }
}
