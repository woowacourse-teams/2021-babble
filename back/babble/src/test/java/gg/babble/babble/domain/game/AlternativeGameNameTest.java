package gg.babble.babble.domain.game;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlternativeGameNameTest {

    private Game game;
    private AlternativeGameName alternativeGameName;

    @BeforeEach
    void setUp() {
        game = new Game("디지투온", Collections.singletonList("화려한 이미지"));
        alternativeGameName = new AlternativeGameName("EZ2ON", game);
        game.addAlternativeName(alternativeGameName);
    }

    @DisplayName("AlternativeGameName 생성 후 Game 추가")
    @Test
    void constructAlternativeGameName() {
        assertThat(game.getAlternativeGameNames().contains(alternativeGameName.getValue())).isTrue();
    }

    @DisplayName("대체 이름 삭제")
    @Test
    void delete() {
        // when
        alternativeGameName.delete();
        // then
        assertThat(alternativeGameName.isDeleted()).isTrue();
    }
}