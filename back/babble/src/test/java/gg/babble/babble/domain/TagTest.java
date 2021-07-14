package gg.babble.babble.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TagTest {

    @DisplayName("태그 이름은 1자 이상 8자 이하다.")
    @Test
    void nameLengthTest() throws Exception {
        assertThatCode(() -> {
            Tag.builder()
                    .name("힝")
                    .build();
            Tag.builder()
                    .name("태그길이여덟글자")
                    .build();
        }).doesNotThrowAnyException();
    }

    @DisplayName("태그 이름은 1자 이하면 예외가 발생한다.")
    @Test
    void nameLengthLessTest() throws Exception {
        assertThatThrownBy(() ->
                Tag.builder()
                        .name("")
                        .build()
        ).isInstanceOf(BabbleLengthException.class);
    }

    @DisplayName("태그 이름은 8자 이상이면 예외가 발생한다.")
    @Test
    void nameLengthOverTest() throws Exception {
        assertThatThrownBy(() ->
                Tag.builder()
                        .name("태그길이가아홉글자")
                        .build()
        ).isInstanceOf(BabbleLengthException.class);
    }
}
