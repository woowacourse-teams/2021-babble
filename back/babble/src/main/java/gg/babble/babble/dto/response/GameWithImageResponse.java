package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.Game;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class GameWithImageResponse {

    private final Long id;
    private final String name;
    private final String thumbnail;
    private final Set<String> alternativeNames;

    public static GameWithImageResponse from(final Game game) {
        return new GameWithImageResponse(game.getId(), game.getName(), game.getImage(), game.getAlternativeGameNames().getNames());
    }

}
