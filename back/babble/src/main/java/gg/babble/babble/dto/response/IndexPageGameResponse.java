package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.Game;
import gg.babble.babble.domain.game.Games;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IndexPageGameResponse {

    private final Long id;
    private final String name;
    private final int headCount;
    private final String thumbnail;
    private final Set<String> alternativeNames;

    public static List<IndexPageGameResponse> listFrom(final Games games) {
        return games.toList()
            .stream()
            .map(IndexPageGameResponse::from)
            .collect(Collectors.toList());
    }

    private static IndexPageGameResponse from(final Game game) {
        return new IndexPageGameResponse(game.getId(), game.getName(), game.userHeadCount(), game.getImage(), game.getAlternativeGameNames().getNames());
    }
}
