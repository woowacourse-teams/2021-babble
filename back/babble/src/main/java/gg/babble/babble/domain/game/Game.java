package gg.babble.babble.domain.game;

import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.room.Rooms;
import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleNotFoundException;
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
import org.hibernate.annotations.Where;

@Getter
@Where(clause = "deleted=false")
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
    @Embedded
    private AlternativeGameNames alternativeGameNames;

    @Column(nullable = false)
    private boolean deleted = false;

    public Game(final String name) {
        this(null, name, DEFAULT_IMAGE, new AlternativeGameNames());
    }

    public Game(final Long id, final String name) {
        this(id, name, DEFAULT_IMAGE, new AlternativeGameNames());
    }

    public Game(final String name, final String image) {
        this(null, name, image);
    }

    public Game(final String name, final String image, final AlternativeGameNames alternativeGameNames) {
        this(null, name, image, alternativeGameNames);
    }

    public Game(final Long id, final String name, final String image) {
        this(id, name, image, new AlternativeGameNames());
    }

    public Game(final Long id, final String name, final String image, final AlternativeGameNames alternativeGameNames) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.alternativeGameNames = alternativeGameNames;
    }

    public int userHeadCount() {
        return rooms.totalHeadCount();
    }

    public void update(final Game target) {
        this.name = target.name;
        this.image = target.image;
        this.alternativeGameNames = target.alternativeGameNames;
    }

    public void addRoom(Room room) {
        rooms.addRoom(room);
    }

    public void addAlternativeName(final AlternativeGameName alternativeGameName) {
        if (hasName(alternativeGameName.getValue())) {
            throw new BabbleDuplicatedException(String.format("이미 존재하는 이름 입니다.(%s)", alternativeGameName.getValue()));
        }

        alternativeGameNames.add(alternativeGameName);

        if (alternativeGameName.getGame() != this) {
            alternativeGameName.setGame(this);
        }
    }

    public void removeAlternativeName(final AlternativeGameName alternativeGameName) {
        if (hasNotName(alternativeGameName.getValue())) {
            throw new BabbleNotFoundException(String.format("존재하지 않는 이름 입니다.(%s)", alternativeGameName.getValue()));
        }

        alternativeGameNames.remove(alternativeGameName);

        if (alternativeGameName.getGame().equals(this)) {
            alternativeGameName.delete();
        }
    }

    public boolean hasName(final String name) {
        return this.name.equals(name) || alternativeGameNames.contains(name);
    }

    public boolean hasNotName(final String name) {
        return !hasName(name);
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