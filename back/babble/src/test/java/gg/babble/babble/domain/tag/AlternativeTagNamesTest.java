package gg.babble.babble.domain.tag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AlternativeTagNamesTest {

    private Tag tag;
    private AlternativeTagNames alternativeTagNames;

    @BeforeEach
    void setUp() {
        tag = new Tag("1시간");
        alternativeTagNames = new AlternativeTagNames();
    }

    @DisplayName("대체 이름 추가")
    @Test
    void add() {
        // given
        final AlternativeTagName alternativeTagName = new AlternativeTagName("1hour", tag);
        // when
        alternativeTagNames.add(alternativeTagName);
        // then
        alternativeTagNames.contains(alternativeTagName.getValue());
    }

    @DisplayName("이미 존재하는 대체 이름 추가시 예외 처리")
    @Test
    void addSameName() {
        final AlternativeTagName alternativeTagName = new AlternativeTagName("1hour", tag);
        alternativeTagNames.add(alternativeTagName);
        assertThatExceptionOfType(BabbleDuplicatedException.class).isThrownBy(() -> alternativeTagNames.add(alternativeTagName));
    }

    @DisplayName("대체 이름 삭제")
    @Test
    void remove() {
        // given
        final AlternativeTagName alternativeTagName = new AlternativeTagName("1hour", tag);
        alternativeTagNames.add(alternativeTagName);
        // when
        alternativeTagNames.remove(alternativeTagName);
        // then
        assertThat(alternativeTagNames.contains(alternativeTagName.getValue())).isFalse();
    }

    @DisplayName("존재하지 않는 대체 이름 삭제시 예외 처리")
    @Test
    void removeNotFoundName() {
        // given
        final AlternativeTagName alternativeTagName = new AlternativeTagName("1hour", tag);
        // then
        assertThatExceptionOfType(BabbleNotFoundException.class).isThrownBy(() -> alternativeTagNames.remove(alternativeTagName));
    }
}
