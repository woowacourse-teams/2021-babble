package gg.babble.babble.dto;

import gg.babble.babble.domain.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameResponse {

    private final Long id;
    private final String name;

    public static GameResponse from(final Game game) {
        return new GameResponse(game.getId(), game.getName());
    }
}
