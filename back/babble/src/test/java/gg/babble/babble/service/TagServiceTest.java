package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
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

    //    // data Loader class 호출을 없애길 희망.
//    // TODO : 프로필에 테스트 data Loader에서만 처리하게 하거나, 모여서 할 이야기라서 일단 keep
//    @Autowired
//    private TagRepository tagRepository;
//
//    @BeforeEach
//    private void prepareDummyTags() {
//        tagRepository.save(Tag.builder()
//                .name("2시간")
//                .build()
//        );
//        tagRepository.save(Tag.builder()
//                .name("솔로랭크")
//                .build()
//        );
//        tagRepository.save(Tag.builder()
//                .name("실버")
//                .build()
//        );
//    }
//
    @DisplayName("존재하지 않는 태그면 예외 처리한다.")
    @Test
    void tagNotFoundTest() {
        assertThatThrownBy(() -> tagService.findById(Long.MAX_VALUE))
            .isInstanceOf(BabbleNotFoundException.class);
    }

    @DisplayName("태그 전체를 불러오면, DB에 존재하는 모든 태그를 불러온다.")
    @Test
    void getAllTags() {

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
}
