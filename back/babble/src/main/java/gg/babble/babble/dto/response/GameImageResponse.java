package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameImageResponse {

    private Long gameId;
    private String image;

    public static GameImageResponse from(final Game game) {
        return new GameImageResponse(game.getId(), game.getImage());
    }
}
