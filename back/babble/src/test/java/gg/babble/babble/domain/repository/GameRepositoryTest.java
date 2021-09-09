package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.game.Game;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.List;
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
        Game game = new Game("게임 이름", "게임 이미지");

        // when
        Game savedGame = gameRepository.save(game);

        // then
        assertThat(savedGame.getId()).isNotNull();
        assertThat(savedGame.getName()).isEqualTo(game.getName());
        assertThat(savedGame.getImage()).isEqualTo(game.getImage());
    }

    @DisplayName("게임 정보 편집을 수행한다.")
    @Test
    void updateGame() {
        // given
        Game game = gameRepository.save(new Game("오래된 게임", "오래된 이미지"));
        Game target = new Game("새로운 게임", "새로운 이미지");

        // when
        game.update(target);
        Game foundGame = gameRepository.findByIdAndDeletedFalse(game.getId())
            .orElseThrow(BabbleNotFoundException::new);

        // then
        assertThat(game).usingRecursiveComparison()
            .isEqualTo(foundGame);
    }

    @DisplayName("게임 삭제를 수행한다.")
    @Test
    void deleteGame() {
        // given
        Game game = gameRepository.save(new Game("게임 이름", "게임 이미지"));

        // then
        assertThat(gameRepository.findByIdAndDeletedFalse(game.getId())).isPresent();
        game.delete();
        assertThat(gameRepository.findByIdAndDeletedFalse(game.getId())).isNotPresent();
    }

    @DisplayName("전체 게임 조회시 게임 삭제된 게임은 조회하지 않는다.")
    @Test
    void findByDeletedFalse() {
        // given
        Game apex_game = gameRepository.save(new Game("에이펙스", "에이펙스 이미지"));
        Game bpex_game = gameRepository.save(new Game("비펙스", "비펙스 이미지"));
        Game cpex_game = gameRepository.save(new Game("씨펙스", "씨펙스 이미지"));

        // when
        List<Game> beforeDeleteGames = gameRepository.findByDeletedFalse();
        bpex_game.delete();
        List<Game> afterDeleteGames = gameRepository.findByDeletedFalse();

        // then
        assertThat(beforeDeleteGames).containsExactly(apex_game, bpex_game, cpex_game);
        assertThat(afterDeleteGames).containsExactly(apex_game, cpex_game);
    }

    @DisplayName("ID로 게임 조회시 게임 삭제된 게임은 조회하지 않는다.")
    @Test
    void findByIdAndDeletedFalse() {
        // given
        Game game = gameRepository.save(new Game("에이펙스", "에이펙스 이미지"));

        // then
        assertThat(gameRepository.findByIdAndDeletedFalse(game.getId())).isPresent();

        game.delete();
        assertThat(gameRepository.findByIdAndDeletedFalse(game.getId())).isNotPresent();
    }
}
