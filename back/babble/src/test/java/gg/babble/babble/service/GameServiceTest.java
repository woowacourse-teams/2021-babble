package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.dto.GameImageResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GameServiceTest extends ApplicationTest {

    private static final String LEAGUE_OF_LEGENDS_URL = "https://static-cdn.jtvnw.net/ttv-boxart/League%20of%20Legends-1080x1436.jpg";
    @Autowired
    private GameService gameService;

    @DisplayName("게임 Id가 없을 경우 예외를 던진다.")
    @Test
    void gameNotFoundTest() {
        assertThatThrownBy(() -> gameService.findById(Long.MAX_VALUE))
            .isInstanceOf(BabbleNotFoundException.class);
    }

    @DisplayName("게임에 해당하는 이미지를 반환한다.")
    @Test
    void findGameImageById() {
        // given
        GameImageResponse expectedResponse = new GameImageResponse(1L, LEAGUE_OF_LEGENDS_URL);

        // then
        assertThat(gameService.findGameImageById(1L)).usingRecursiveComparison()
            .isEqualTo(expectedResponse);
    }
}
