package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.tag.Tag;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class TagRepositoryTest {

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
        List<Tag> beforeDeleteTags = tagRepository.findAll();
        bpex_tag.delete();
        List<Tag> afterDeleteTags = tagRepository.findAll();

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
        assertThat(tagRepository.findById(tag.getId())).isPresent();
        tag.delete();

        // then
        assertThat(tagRepository.findById(tag.getId())).isNotPresent();
    }
}
