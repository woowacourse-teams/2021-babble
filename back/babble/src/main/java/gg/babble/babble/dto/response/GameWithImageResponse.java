package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.AlternativeGameNames;
import gg.babble.babble.domain.game.Game;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameWithImageResponse {

    private final Long id;
    private final String name;
    private final List<String> images;
    private final List<AlternativeGameNameResponse> alternativeNames;

    public static GameWithImageResponse from(final Game game) {
        return new GameWithImageResponse(
            game.getId(),
            game.getName(),
            game.getImages(),
            convertToResponse(game.getAlternativeGameNames())
        );
    }

    private static List<AlternativeGameNameResponse> convertToResponse(AlternativeGameNames alternativeGameNames) {
        return alternativeGameNames.getElements()
            .stream()
            .map(AlternativeGameNameResponse::from)
            .collect(Collectors.toList());
    }
}
