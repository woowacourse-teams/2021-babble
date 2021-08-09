package gg.babble.babble.domain;

import gg.babble.babble.domain.room.Rooms;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Game {

    private static final String DEFAULT_IMAGE = "https://static-cdn.jtvnw.net/ttv-static/404_boxart-1080x1436.jpg";
    @Embedded
    private final Rooms rooms = new Rooms();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "게임 이름은 Null 일 수 없습니다.")
    private String name;
    @NotNull(message = "게임 이미지는 Null 일 수 없습니다.")
    private String image;
    @Column(nullable = false)
    private boolean deleted = false;

    public Game(final String name) {
        this(null, name, DEFAULT_IMAGE);
    }

    public Game(final Long id, final String name) {
        this(id, name, DEFAULT_IMAGE);
    }

    public Game(final String name, final String image) {
        this(null, name, image);
    }

    public Game(final Long id, final String name, final String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int userHeadCount() {
        return rooms.totalHeadCount();
    }

    public void update(final Game target) {
        this.name = target.name;
        this.image = target.image;
    }

    public void delete() {
        this.deleted = true;
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
