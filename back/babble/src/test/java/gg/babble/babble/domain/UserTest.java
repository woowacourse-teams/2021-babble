package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

    @DisplayName("같은 닉네임이면 같은 아바타가 나온다.")
    @Test
    void avatarByNickname() {
        // when
        User user1 = new User("루트");
        User user2 = new User("루트");

        // then
        assertThat(user1.getAvatar()).isEqualTo(user2.getAvatar());
    }
}
