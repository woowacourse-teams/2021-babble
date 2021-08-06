package gg.babble.babble.dto.response;

import gg.babble.babble.domain.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameWithImageResponse {

    private final Long id;
    private final String name;
    private final String thumbnail;

    public static GameWithImageResponse from(final Game game) {
        return new GameWithImageResponse(game.getId(), game.getName(), game.getImage());
    }
}
