package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

public class GameRepositoryTest extends ApplicationTest {

    @Autowired
    private GameRepository gameRepository;

    @DisplayName("게임 더미 데이터를 확인한다.")
    @ParameterizedTest
    @ValueSource(strings = {"League Of Legend", "Overwatch", "Apex Legend"})
    void dummyGameTest(final String gameName) {
        List<Game> game = gameRepository.findByName(gameName);
        assertThat(game).isNotEmpty();
    }
}
