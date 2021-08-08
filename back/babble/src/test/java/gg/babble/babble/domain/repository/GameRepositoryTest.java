package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.Game;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @DisplayName("게임 정보 편집을 수행한다.")
    @Test
    void updateGame() {
        // given
        Game game = gameRepository.save(new Game("오래된 게임", "오래된 이미지"));
        Game target = new Game("새로운 게임", "새로운 이미지");

        // when
        game.update(target);
        Game foundGame = gameRepository.findById(game.getId())
            .orElseThrow(BabbleNotFoundException::new);

        // then
        assertThat(game).usingRecursiveComparison()
            .isEqualTo(foundGame);
    }
}
