package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MaxHeadCountTest {

    @DisplayName("최대 참가 인원은 2이상 20이하의 자연수다.")
    @ParameterizedTest
    @ValueSource(ints = {2, 10, 20})
    void constructMaxHeadCountTest(int value) {
        assertThatCode(() -> new MaxHeadCount(value)).doesNotThrowAnyException();
    }

    @DisplayName("최대 참가 인원이 2미만 또는 20초과이면 예외 처리한다.")
    @Test
    void constructMaxHeadCountExceptionTest() {
        assertThatThrownBy(() -> new MaxHeadCount(1)).isInstanceOf(BabbleIllegalArgumentException.class);
        assertThatThrownBy(() -> new MaxHeadCount(21)).isInstanceOf(BabbleIllegalArgumentException.class);
    }
}
