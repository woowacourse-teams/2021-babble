package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.tag.AlternativeTagName;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.dto.request.TagCreateRequest;
import gg.babble.babble.dto.request.TagUpdateRequest;
import gg.babble.babble.dto.response.TagResponse;
import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @DisplayName("태그를 생성할 때")
    @Nested
    class createTag {

        @DisplayName("태그 이름과 대체 이름간 중복이 없을 경우 정상적으로 생성된다.")
        @Test
        void createSuccess() {
            // given
            String tagName = "루트";
            List<String> alternativeNames = Arrays.asList("피에 굶주린", "죽음의 사도", "어둠의다크니스 루트");
            TagCreateRequest request = new TagCreateRequest(tagName, alternativeNames);

            // when
            TagResponse response = tagService.createTag(request);

            // then
            assertThat(response.getId()).isNotNull();
            assertThat(response.getName()).isEqualTo(tagName);
            assertThat(response.getAlternativeNames()).containsAll(alternativeNames);
        }

        @DisplayName("대체 이름간 중복이 존재할 경우 예외가 발생한다.")
        @Test
        void alternativeNameDuplicateException() {
            // given
            String tagName = "포츈";
            List<String> alternativeNames = Arrays.asList("그가 화장실에 가면 기적이 일어난다", "토일렛 디버거 포츈", "토일렛 디버거 포츈");
            TagCreateRequest request = new TagCreateRequest(tagName, alternativeNames);

            // when, then
            assertThatThrownBy(() -> tagService.createTag(request))
                .isExactlyInstanceOf(BabbleDuplicatedException.class);
        }

        @DisplayName("태그 이름과 대체 이름간 중복이 존재할 경우 예외가 발생한다.")
        @Test
        void tagNameDuplicateWithAlternativeNameException() {
            // given
            String tagName = "와일더";
            List<String> alternativeNames = Arrays.asList("크윽, 모두 도망쳐!!", "내 왼팔의 흑염룡이.. 크윽!!", "와일더");
            TagCreateRequest request = new TagCreateRequest(tagName, alternativeNames);

            // when, then
            assertThatThrownBy(() -> tagService.createTag(request))
                .isExactlyInstanceOf(BabbleDuplicatedException.class);
        }
    }

    @DisplayName("태그 정보를 수정할 때")
    @Nested
    class UpdateTag {

        @DisplayName("id에 해당하는 태그가 존재하면 태그 정보를 수정한다.")
        @Test
        void updateTag() {
            // given
            Tag tag = tagRepository.save(new Tag("피터파커"));
            AlternativeTagName alternativeTagName = new AlternativeTagName("노 웨이 홈", tag);

            String updateTagName = "피똥파커";
            List<String> updateAlternativeTagNames = Arrays.asList("웨", "쳐", "피", "똥");

            // when
            TagUpdateRequest request = new TagUpdateRequest(updateTagName, updateAlternativeTagNames);
            TagResponse response = tagService.updateTag(tag.getId(), request);

            // then
            assertThat(response.getId()).isEqualTo(tag.getId());
            assertThat(response.getName()).isEqualTo(updateTagName);
            assertThat(response.getAlternativeNames()).isEqualTo(updateAlternativeTagNames);
        }

        @DisplayName("id에 해당하는 태그가 존재하지 않으면 예외가 발생한다.")
        @Test
        void updateTagException() {
            // given
            String updateTagName = "피똥파커";
            List<String> updateAlternativeTagNames = Arrays.asList("웨", "쳐", "피", "똥");

            // when, then
            TagUpdateRequest request = new TagUpdateRequest(updateTagName, updateAlternativeTagNames);
            assertThatThrownBy(() -> tagService.updateTag(Long.MAX_VALUE, request))
                .isExactlyInstanceOf(BabbleNotFoundException.class);
        }
    }

    @DisplayName("태그를 삭제할 때")
    @Nested
    class DeleteTag {

        @DisplayName("id에 해당하는 태그가 존재하면 태그를 삭제한다.")
        @Test
        void updateTag() {
            // given
            Tag tag = tagRepository.save(new Tag("피터파커"));
            AlternativeTagName alternativeTagName = new AlternativeTagName("노 웨이 홈", tag);

            // when
            assertThat(tagRepository.findById(tag.getId())).isPresent();
            tagService.deleteTag(tag.getId());

            // then
            assertThat(tagRepository.findById(tag.getId())).isNotPresent();
        }

        @DisplayName("id에 해당하는 태그가 존재하지 않으면 예외가 발생한다.")
        @Test
        void updateTagException() {
            // when, then
            assertThatThrownBy(() -> tagService.deleteTag(Long.MAX_VALUE))
                .isExactlyInstanceOf(BabbleNotFoundException.class);
        }
    }
}
