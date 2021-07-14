package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class GameTest {

    @Autowired
    private GameRepository gameRepository;

    @DisplayName("게임 더미 데이터를 확인한다.")
    @ParameterizedTest
    @CsvSource({"1, League Of Legend", "2, Overwatch", "3, Apex Legend"})
    void dummyGameTest(Long id, String gameName) {
        Optional<Game> game = gameRepository.findById(id);
        assertThat(game.isPresent()).isTrue();
        assertThat(game.get().getName()).isEqualTo(gameName);
    }
}
