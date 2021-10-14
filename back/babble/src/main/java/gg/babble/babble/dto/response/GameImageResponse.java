package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.Game;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameImageResponse {

    private Long gameId;
    private List<String> images;

    public static GameImageResponse from(final Game game) {
        return new GameImageResponse(game.getId(), game.getImages());
    }
}
