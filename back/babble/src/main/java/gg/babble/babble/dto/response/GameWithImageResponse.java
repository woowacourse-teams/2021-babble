package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.Game;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameWithImageResponse {

    private final Long id;
    private final String name;
    private final List<String> images;
    private final List<String> alternativeNames;

    public static GameWithImageResponse from(final Game game) {
        return new GameWithImageResponse(game.getId(), game.getName(), game.getImages(), game.getAlternativeGameNames().getNames());
    }

}
