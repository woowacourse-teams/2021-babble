package gg.babble.babble.domain.tag;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import gg.babble.babble.exception.BabbleLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TagNameTest {

    @DisplayName("1자이상 20자이하의 길이일 경우 생성")
    @ValueSource(strings = {"고기가가가기고", "ㅋ", "very long tag name ."})
    @ParameterizedTest
    void tagNameLengthTest(final String name) {
        assertThatCode(() -> new TagName(name)).doesNotThrowAnyException();
    }

    @DisplayName("1자보다 짧거나 20자보다 길 경우 예외처리")
    @ValueSource(strings = {"", "very long tag name w."})
    @ParameterizedTest
    void InvalidTagNameLengthTest(final String name) {
        assertThatExceptionOfType(BabbleLengthException.class).isThrownBy(() -> new TagName(name));
    }
}
