package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.game.Game;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @DisplayName("게임 추가를 수행한다.")
    @Test
    void saveGame() {
        // given
        Game game = new Game("게임 이름", Collections.singletonList("게임 이미지"));
        game.addNames(Collections.singletonList("새 게임"));

        // when
        Game savedGame = gameRepository.save(game);

        // then
        assertThat(savedGame.getId()).isNotNull();
        assertThat(savedGame.getName()).isEqualTo(game.getName());
        assertThat(savedGame.getImages()).isEqualTo(game.getImages());
        assertThat(savedGame.getAlternativeNames()).hasSize(1).contains("새 게임");
    }

    @DisplayName("게임 정보 편집을 수행한다.")
    @Test
    void updateGame() {
        // given
        Game game = gameRepository.save(new Game("오래된 게임", Collections.singletonList("오래된 이미지")));
        Game target = new Game("새로운 게임", Collections.singletonList("오래된 이미지"));

        // when
        game.update(target.getName(), new ArrayList<>(), target.getImages());
        Game foundGame = gameRepository.findById(game.getId())
            .orElseThrow(BabbleNotFoundException::new);

        // then
        assertThat(game).usingRecursiveComparison()
            .isEqualTo(foundGame);
    }
}
