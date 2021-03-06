package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.AlternativeGameNames;
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
public class IndexPageGameResponse {

    private Long id;
    private String name;
    private int headCount;
    private List<String> images;
    private List<AlternativeGameNameResponse> alternativeNames;

    public static List<IndexPageGameResponse> listFrom(final Games games) {
        return games.toList()
            .stream()
            .map(IndexPageGameResponse::from)
            .collect(Collectors.toList());
    }

    private static IndexPageGameResponse from(final Game game) {
        return new IndexPageGameResponse(
            game.getId(),
            game.getName(),
            game.userHeadCount(),
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
