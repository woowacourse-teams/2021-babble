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
        alternativeTagName = new AlternativeTagName("1hour", tag);
    }

    @DisplayName("AlternativeTagName을 생성하면 자동으로 Tag에 추가됨")
    @Test
    void constructAlternativeGameName() {
        assertThat(tag.getAlternativeTagNames().contains(alternativeTagName.getValue())).isTrue();
    }

    @DisplayName("대체 이름의 게임을 변경")
    @Test
    void setGame() {
        final Tag anotherTag = new Tag("2시간");
        alternativeTagName.setTag(anotherTag);

        assertThat(anotherTag.getAlternativeTagNames().contains(alternativeTagName.getValue())).isTrue();
        assertThat(tag.getAlternativeTagNames().contains(alternativeTagName.getValue())).isFalse();
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
