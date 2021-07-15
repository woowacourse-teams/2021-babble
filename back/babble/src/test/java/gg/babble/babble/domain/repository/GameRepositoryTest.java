package gg.babble.babble.domain.repository;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GameRepositoryTest extends ApplicationTest {

    @Autowired
    private GameRepository gameRepository;

    @DisplayName("게임 더미 데이터를 확인한다.")
    @ParameterizedTest
    @CsvSource({"1, League Of Legend", "2, Overwatch", "3, Apex Legend"})
    void dummyGameTest(final Long id, final String gameName) {
        Optional<Game> game = gameRepository.findById(id);
        assertThat(game.isPresent()).isTrue();
        assertThat(game.get().getName()).isEqualTo(gameName);
    }
}
