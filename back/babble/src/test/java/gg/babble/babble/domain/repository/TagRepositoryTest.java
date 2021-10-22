package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.tag.AlternativeTagName;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.tag.TagName;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
public class TagRepositoryTest {

    @Autowired
    private AlternativeTagNameRepository alternativeTagNameRepository;

    @Autowired
    private TagRepository tagRepository;

    @DisplayName("태그를 생성한다.")
    @Test
    void saveTag() {
        // given
        Tag tag = new Tag("새로운 태그");

        // when
        Tag savedTag = tagRepository.save(tag);

        // then
        assertThat(savedTag.getId()).isNotNull();
        assertThat(savedTag.getName()).isEqualTo(tag.getName());
    }

    @DisplayName("전체 태그 조회시 삭제된 태그는 조회하지 않는다.")
    @Test
    void findByDeletedFalse() {
        // given
        Tag apex_tag = tagRepository.save(new Tag("에이펙스"));
        Tag bpex_tag = tagRepository.save(new Tag("비펙스"));
        Tag cpex_tag = tagRepository.save(new Tag("씨펙스"));

        // when
        List<Tag> beforeDeleteTags = tagRepository.findByDeletedFalse();
        bpex_tag.delete();
        List<Tag> afterDeleteTags = tagRepository.findByDeletedFalse();

        // then
        assertThat(beforeDeleteTags).containsExactly(apex_tag, bpex_tag, cpex_tag);
        assertThat(afterDeleteTags).containsExactly(apex_tag, cpex_tag);
    }

    @DisplayName("ID로 태그 조회시 삭제된 태그는 조회하지 않는다.")
    @Test
    void findByIdAndDeletedFalse() {
        // given
        Tag tag = tagRepository.save(new Tag("똥겜"));

        // when
        assertThat(tagRepository.findByIdAndDeletedFalse(tag.getId())).isPresent();
        tag.delete();

        // then
        assertThat(tagRepository.findByIdAndDeletedFalse(tag.getId())).isNotPresent();
    }

    @DisplayName("태그 이름을 검색한다.")
    @Test
    void findByKeyword() {
        Tag tag1 = tagRepository.save(new Tag("2시간"));
        Tag tag2 = tagRepository.save(new Tag("3hours"));
        tagRepository.save(new Tag("30분"));

        alternativeTagNameRepository.save(new AlternativeTagName(new TagName("2hours"), tag1));
        alternativeTagNameRepository.save(new AlternativeTagName(new TagName("3시간"), tag2));

        List<Tag> foundTags = tagRepository.findAllByKeyword("시간", PageRequest.of(0, 100));

        assertThat(foundTags).hasSize(2)
            .contains(tag1, tag2);
    }

    @DisplayName("태그 이름 검색에 페이지네이션을 적용한다.")
    @Test
    void findByKeywordLimit() {
        for (int i = 0; i < 150; i++) {
            tagRepository.save(new Tag(i + "시간"));
        }

        List<Tag> foundTags = tagRepository.findAllByKeyword("시간", PageRequest.of(0, 100));

        assertThat(foundTags).hasSize(100);
    }
}
