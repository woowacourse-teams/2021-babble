package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.Game;
import gg.babble.babble.domain.game.Games;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameNameResponse {

    private Long id;
    private String name;

    public static List<GameNameResponse> listFrom(final Games games) {
        return games.toList().stream()
            .map(GameNameResponse::from)
            .collect(Collectors.toList());
    }

    private static GameNameResponse from(final Game game) {
        return new GameNameResponse(game.getId(), game.getName());
    }
}
