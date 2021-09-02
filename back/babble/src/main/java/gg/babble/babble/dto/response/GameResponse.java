package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {

    private Long id;
    private String name;

    public static GameResponse from(final Game game) {
        return new GameResponse(game.getId(), game.getName());
    }
}
