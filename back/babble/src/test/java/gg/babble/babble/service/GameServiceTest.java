package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.dto.GameImageResponse;
import gg.babble.babble.dto.IndexPageGameResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GameServiceTest extends ApplicationTest {

    private static final String LEAGUE_OF_LEGENDS = "League Of Legends";
    private static final String OVERWATCH = "Overwatch";
    private static final String APEX_LEGEND = "Apex Legend";
    private static final String LEAGUE_OF_LEGENDS_URL = "https://static-cdn.jtvnw.net/ttv-boxart/League%20of%20Legends-1080x1436.jpg";
    private static final String DEFAULT_URL = "https://static-cdn.jtvnw.net/ttv-static/404_boxart-1080x1436.jpg";

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

    @DisplayName("전체 게임 이미지 목록을 반환한다.")
    @Test
    void gameImages() {
        // given
        List<GameImageResponse> expectedResponses = Arrays.asList(
            new GameImageResponse(1L, LEAGUE_OF_LEGENDS_URL),
            new GameImageResponse(2L, "https://static-cdn.jtvnw.net/ttv-static/404_boxart-1080x1436.jpg"),
            new GameImageResponse(3L, "https://static-cdn.jtvnw.net/ttv-static/404_boxart-1080x1436.jpg")
        );

        // then
        assertThat(gameService.findAllGameImages()).usingRecursiveComparison()
            .isEqualTo(expectedResponses);
    }

    @DisplayName("전체 게임 리스트를 반환한다.")
    @Test
    void findAllGames() {
        // when
        List<IndexPageGameResponse> expectedResponses = Arrays.asList(
            new IndexPageGameResponse(1L, LEAGUE_OF_LEGENDS, 1, LEAGUE_OF_LEGENDS_URL),
            new IndexPageGameResponse(2L, OVERWATCH, 0, DEFAULT_URL),
            new IndexPageGameResponse(3L, APEX_LEGEND, 0, DEFAULT_URL)
        );
        // then
        assertThat(gameService.findSortedGames()).usingRecursiveComparison()
            .isEqualTo(expectedResponses);
    }
}
