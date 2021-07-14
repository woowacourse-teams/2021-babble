package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

public class Game {

    @Autowired
    private GameRepository gameRepository;

    @DisplayName("게임 더미 데이터를 확인한다.")
    @ParameterizedTest
    @CsvSource({"1L, League Of Legend", "2L, Overwatch", "3L, Apex Legend"})
    void dummyGameTest(Long id, String gameName) {
        Game game = gameRepository.findById(id);
        assertThat(game.name()).equals(gameName);
    }
}
