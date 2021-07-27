package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.dto.UserRequest;
import gg.babble.babble.dto.UserResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends ApplicationTest {

    private static final String FORTUNE = "fortune";
    private static final String DEFAULT_URL = "https://hyeon9mak.github.io/assets/images/9vatar.png";
    @Autowired
    private UserService userService;

    @DisplayName("유저 Id가 없을 경우 예외를 던진다.")
    @Test
    void userNotFoundTest() {
        assertThatThrownBy(() -> userService.findById(Long.MAX_VALUE))
            .isInstanceOf(BabbleNotFoundException.class);
    }

    @DisplayName("유저를 저장한다.")
    @Test
    void saveUser() {
        // when
        UserResponse response = userService.save(new UserRequest(FORTUNE));

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getNickname()).isEqualTo(FORTUNE);
        assertThat(response.getAvatar()).isEqualTo(DEFAULT_URL);
    }
}
