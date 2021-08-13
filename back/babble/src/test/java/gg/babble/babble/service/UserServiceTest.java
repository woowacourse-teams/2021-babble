package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.dto.request.UserRequest;
import gg.babble.babble.dto.response.UserResponse;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends ApplicationTest {

    private static final String FORTUNE = "fortune";
    private static final String FORTUNE_AVATAR = "https://d2bidcnq0n74fu.cloudfront.net/img/users/profiles/profile57.png";
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
        assertThat(response.getAvatar()).isEqualTo(FORTUNE_AVATAR);
    }

    @DisplayName("닉네임으로 한글, 영어, 숫자, 공백을 사용할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"루트", "root", "루트가 최고다", "roo트", "루트5"})
    void nickname(final String nickname) {
        assertThatCode(() -> userService.save(new UserRequest(nickname))).doesNotThrowAnyException();
    }

    @DisplayName("닉네임에 특수 문자를 사용할 경우 예외를 던진다.")
    @Test
    void nicknameWithSpecialCharacter() {
        assertThatThrownBy(() -> userService.save(new UserRequest("루트가짱*")))
            .isExactlyInstanceOf(BabbleIllegalArgumentException.class);
    }

    @DisplayName("유저 닉네임의 길이가 1이상 20이하가 아니면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "abcdefghijklmnopqrstuvwxyz"})
    void nicknameLength(String nickname) {
        assertThatThrownBy(() -> userService.save(new UserRequest(nickname)))
            .isExactlyInstanceOf(BabbleIllegalArgumentException.class);
    }
}
