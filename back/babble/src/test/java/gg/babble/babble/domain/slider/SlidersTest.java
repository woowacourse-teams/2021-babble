package gg.babble.babble.domain.slider;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SlidersTest {

    private Sliders sliders;

    @BeforeEach
    public void setUp() {
        sliders = new Sliders(Arrays.asList(
            new Slider("test1/image"),
            new Slider("test2/image"),
            new Slider("test3/image")
        ));
    }

    @DisplayName("중복된 아이디를 포함한 요청 시 예외가 발생한다.")
    @Test
    void duplicateId() {
        List<Long> ids = Arrays.asList(1L, 2L, 2L);

        assertThatThrownBy(() -> sliders.changeOrder(ids)).isInstanceOf(BabbleDuplicatedException.class);
    }

    @DisplayName("존재하지 않는 아이디를 포함한 요청 시 예외가 발생한다.")
    @Test
    void notExistId() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);

        assertThatThrownBy(() -> sliders.changeOrder(ids)).isInstanceOf(BabbleIllegalArgumentException.class);
    }

    @DisplayName("등록된 모든 아이디를 포함하지 요청 시 예외가 발생한다.")
    @Test
    void notEnoughId() {
        List<Long> ids = Arrays.asList(1L, 2L);

        assertThatThrownBy(() -> sliders.changeOrder(ids)).isInstanceOf(BabbleIllegalArgumentException.class);
    }
}
