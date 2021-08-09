package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.user.Nickname;
import gg.babble.babble.domain.user.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryTest extends ApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저 더미 데이터를 확인한다.")
    @ParameterizedTest
    @ValueSource(strings = {"루트", "와일더", "현구막"})
    void dummyUserTest(final String name) {
        List<User> users = userRepository.findByNickname(new Nickname(name));
        assertThat(users).isNotEmpty();
    }
}
