package gg.babble.babble.domain.tag;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlternativeTagNameTest {

    private Tag tag;
    private AlternativeTagName alternativeTagName;

    @BeforeEach
    void setUp() {
        tag = new Tag("1시간");
        alternativeTagName = new AlternativeTagName(new TagName("1hour"), tag);
        tag.addAlternativeName(alternativeTagName);
    }

    @DisplayName("AlternativeTagName을 생성 후 Tag 추가")
    @Test
    void constructAlternativeGameName() {
        assertThat(tag.getAlternativeTagNames().contains(alternativeTagName.getValue())).isTrue();
    }

    @DisplayName("대체 이름 삭제")
    @Test
    void delete() {
        // when
        alternativeTagName.delete();

        // then
        assertThat(tag.hasName(alternativeTagName.getValue())).isFalse();
        assertThat(alternativeTagName.isDeleted()).isTrue();
    }
}
