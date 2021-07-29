package gg.babble.babble.domain;

import gg.babble.babble.domain.room.Rooms;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Game {

    private static final String DEFAULT_IMAGE = "https://static-cdn.jtvnw.net/ttv-static/404_boxart-1080x1436.jpg";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "게임 이름은 Null 일 수 없습니다.")
    private String name;
    @NotNull(message = "게임 이미지는 Null 일 수 없습니다.")
    private String image;
    @Embedded
    private final Rooms rooms = new Rooms();

    public Game(final String name) {
        this(null, name, DEFAULT_IMAGE);
    }

    public Game(final Long id, final String name) {
        this(id, name, DEFAULT_IMAGE);
    }

    public Game(final String name, final String image) {
        this(null, name, image);
    }

    public int userHeadCount() {
        return rooms.totalHeadCount();
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
