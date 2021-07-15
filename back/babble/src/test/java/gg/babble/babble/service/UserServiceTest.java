package gg.babble.babble.service;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserServiceTest extends ApplicationTest {

    @Autowired
    private UserService userService;

    @DisplayName("유저 Id가 없을 경우 예외를 던진다.")
    @Test
    void gameNotFoundTest() {
        assertThatThrownBy(() -> userService.findById(Long.MAX_VALUE))
                .isInstanceOf(BabbleNotFoundException.class);
    }
}
