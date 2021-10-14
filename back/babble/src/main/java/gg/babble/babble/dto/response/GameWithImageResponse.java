package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.AlternativeGameNames;
import gg.babble.babble.domain.game.Game;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameWithImageResponse {

    private Long id;
    private String name;
    private List<String> images;
    private List<AlternativeGameNameResponse> alternativeNames;

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
