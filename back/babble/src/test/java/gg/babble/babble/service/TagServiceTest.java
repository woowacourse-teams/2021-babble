package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.exception.BabbleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TagServiceTest {

    @Autowired
    private TagService tagService;

    @DisplayName("존재하지 않는 태그이면 예외 처리한다.")
    @Test
    void tagNotFoundTest() {
        assertThatThrownBy(() -> tagService.findById("쏙옙뷁훑"))
            .isInstanceOf(BabbleNotFoundException.class);
    }

}
