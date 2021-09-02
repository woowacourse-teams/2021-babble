package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.Game;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameWithImageResponse {

    private Long id;
    private String name;
    private String thumbnail;
    private Set<String> alternativeNames;

    public static GameWithImageResponse from(final Game game) {
        return new GameWithImageResponse(game.getId(), game.getName(), game.getImage(), game.getAlternativeNames().getNames());
    }

}
