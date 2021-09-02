package gg.babble.babble.domain.game;

import java.util.Comparator;
import java.util.List;

public class Games {

    private final List<Game> games;

    public Games(final List<Game> games) {
        this.games = games;
    }

    public void sortedByHeadCount() {
        games.sort(Comparator.comparing(Game::userHeadCount).reversed());
    }

    public List<Game> toList() {
        return games;
    }
}
