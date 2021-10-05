package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.Game;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameImageResponse {

    private final Long gameId;
    private final List<String> images;

    public static GameImageResponse from(final Game game) {
        return new GameImageResponse(game.getId(), game.getImages());
    }
}
