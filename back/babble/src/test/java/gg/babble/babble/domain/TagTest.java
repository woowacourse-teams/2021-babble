package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.exception.BabbleLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TagTest {

    @DisplayName("태그 이름은 1자 이상 8자 이하다.")
    @ParameterizedTest
    @ValueSource(strings = {"힝", "태그길이여덟글자"})
    void nameLengthTest(String name) {
        assertThatCode(() -> new Tag(name)).doesNotThrowAnyException();
    }

    @DisplayName("태그 이름은 1자 이하면 예외가 발생한다.")
    @Test
    void nameLengthLessTest() {
        assertThatThrownBy(() -> new Tag("")).isInstanceOf(BabbleLengthException.class);
    }

    @DisplayName("태그 이름은 8자 이상이면 예외가 발생한다.")
    @Test
    void nameLengthOverTest() {
        assertThatThrownBy(() -> new Tag("태그길이가아홉글자")).isInstanceOf(BabbleLengthException.class);
    }
}
