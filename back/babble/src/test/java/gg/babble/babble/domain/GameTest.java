package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GameTest {

    @DisplayName("게임 정보 편집")
    @Test
    void updateGame() {
        // given
        Game game = new Game(1L, "오래된 게임", "오래된 이미지");
        Game target = new Game("새로운 게임", "새로운 이미지");

        // when
        game.update(target);

        // then
        assertThat(game.getId()).isEqualTo(1L);
        assertThat(game.getName()).isEqualTo(target.getName());
        assertThat(game.getImage()).isEqualTo(target.getImage());
    }
}