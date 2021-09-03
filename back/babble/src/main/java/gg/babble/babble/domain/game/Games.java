package gg.babble.babble.domain.game;

import java.util.Comparator;
import java.util.List;

public class Games {

    private final List<Game> elements;

    public Games(final List<Game> elements) {
        this.elements = elements;
    }

    public void sortedByHeadCount() {
        elements.sort(Comparator.comparing(Game::userHeadCount).reversed());
    }

    public List<Game> toList() {
        return elements;
    }
}
