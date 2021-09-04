package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class GameResponse {

    private final Long id;
    private final String name;

    public static GameResponse from(final Game game) {
        return new GameResponse(game.getId(), game.getName());
    }
}
