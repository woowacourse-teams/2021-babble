package gg.babble.babble.domain.repository;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest extends ApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저 더미 데이터를 확인한다.")
    @ParameterizedTest
    @CsvSource({"1, 루트", "2, 와일더", "3, 현구막"})
    void dummyUserTest(final Long id, final String name) {
        Optional<User> user = userRepository.findById(id);
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getName()).isEqualTo(name);
    }
}
