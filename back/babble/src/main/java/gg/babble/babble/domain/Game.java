package gg.babble.babble.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Game {

    private static final String DEFAULT_IMAGE = "https://static-cdn.jtvnw.net/ttv-static/404_boxart-1080x1436.jpg";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String image;

    public Game(@NonNull final String name) {
        this(null, name, DEFAULT_IMAGE);
    }

    public Game(final Long id, @NonNull final String name) {
        this(id, name, DEFAULT_IMAGE);
    }

    public Game(@NonNull final String name, @NonNull final String image) {
        this(null, name, image);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
