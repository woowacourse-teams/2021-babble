package gg.babble.babble.service;

import gg.babble.babble.domain.Tag;
import gg.babble.babble.domain.TagRepository;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag findById(String name) {
        return tagRepository.findById(name)
                .orElseThrow(() -> new BabbleNotFoundException("존재하지 않는 태그입니다."));
    }
}
