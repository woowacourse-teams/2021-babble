package gg.babble.babble.domain.game;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlternativeGameNameTest {

    private Game game;
    private AlternativeGameName alternativeGameName;

    @BeforeEach
    void setUp() {
        game = new Game("디지투온", "화려한이미지");
        alternativeGameName = new AlternativeGameName("EZ2ON", game);
    }

    @DisplayName("AlternativeGameName을 생성하면 자동으로 Game에 추가됨")
    @Test
    void constructAlternativeGameName() {
        assertThat(game.getAlternativeGameNames().contains(alternativeGameName.getValue())).isTrue();
    }

    @DisplayName("대체 이름의 게임을 변경")
    @Test
    void setGame() {
        final Game anotherGame = new Game("EZ2DJ", "더 화려한 이미지");
        alternativeGameName.setGame(anotherGame);

        assertThat(anotherGame.getAlternativeGameNames().contains(alternativeGameName.getValue())).isTrue();
        assertThat(game.getAlternativeGameNames().contains(alternativeGameName.getValue())).isFalse();
    }

    @DisplayName("대체 이름 삭제")
    @Test
    void delete() {
        // when
        alternativeGameName.delete();
        // then
        assertThat(game.hasName(alternativeGameName.getValue())).isFalse();
        assertThat(alternativeGameName.isDeleted()).isTrue();
    }
}