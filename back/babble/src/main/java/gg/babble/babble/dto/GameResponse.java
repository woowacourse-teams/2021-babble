package gg.babble.babble.dto;

import gg.babble.babble.domain.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameResponse {

    private Long id;
    private String name;

    public static GameResponse from(final Game game) {
        return new GameResponse(game.getId(), game.getName());
    }
}
