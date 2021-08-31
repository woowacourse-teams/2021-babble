package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.tag.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
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
}
