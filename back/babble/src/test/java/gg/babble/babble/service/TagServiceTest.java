package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.repository.TagRepository;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.dto.response.TagResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TagServiceTest extends ApplicationTest {

    @Autowired
    private TagService tagService;

    @DisplayName("태그 전체를 불러오면, DB에 존재하는 모든 태그를 불러온다.")
    @Test
    void getAllTags() {
        prepareDummyTags();

        // given
        List<String> expectedTags = Arrays.asList("실버", "2시간", "솔로랭크");

        // when
        List<String> allTags = tagService.findAll()
            .stream()
            .map(TagResponse::getName)
            .collect(Collectors.toList());

        // then
        assertThat(expectedTags).usingRecursiveComparison().isEqualTo(allTags);
    }

    private void prepareDummyTags() {
        tagRepository.save(new Tag("실버"));
        tagRepository.save(new Tag("2시간"));
        tagRepository.save(new Tag("솔로랭크"));
    }
}
