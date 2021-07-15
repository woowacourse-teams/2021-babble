package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GameServiceTest extends ApplicationTest {

    @Autowired
    private GameService gameService;

    @DisplayName("게임 Id가 없을 경우 예외를 던진다.")
    @Test
    void gameNotFoundTest() {
        assertThatThrownBy(() -> gameService.findById(Long.MAX_VALUE))
            .isInstanceOf(BabbleNotFoundException.class);
    }
}
