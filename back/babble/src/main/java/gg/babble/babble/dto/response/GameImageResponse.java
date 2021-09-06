package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameImageResponse {

    private final Long gameId;
    private final String image;

    public static GameImageResponse from(final Game game) {
        return new GameImageResponse(game.getId(), game.getImage());
    }
}
