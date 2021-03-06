package gg.babble.babble.domain.game;

import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.room.Rooms;
import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@SQLDelete(sql = "UPDATE game SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Game {

    private static final String DEFAULT_IMAGE = "https://static-cdn.jtvnw.net/ttv-static/404_boxart-1080x1436.jpg";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "게임 이름은 Null 일 수 없습니다.")
    private String name;

    @Embedded
    private AlternativeGameNames alternativeGameNames;

    @NotNull(message = "게임 이미지는 Null 일 수 없습니다.")
    @ElementCollection
    @CollectionTable(name = "gameImages", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "game_image")
    private List<String> images;

    @Column(nullable = false)
    private final boolean deleted = false;

    @Embedded
    private final Rooms rooms = new Rooms();

    public Game(final String name) {
        this(null, name, Collections.singletonList(DEFAULT_IMAGE), new AlternativeGameNames());
    }

    public Game(final Long id, final String name) {
        this(id, name, Collections.singletonList(DEFAULT_IMAGE), new AlternativeGameNames());
    }

    public Game(final String name, final List<String> images) {
        this(null, name, images);
    }

    public Game(final String name, final List<String> images, final AlternativeGameNames alternativeGameNames) {
        this(null, name, images, alternativeGameNames);
    }

    public Game(final Long id, final String name, final List<String> images) {
        this(id, name, images, new AlternativeGameNames());
    }

    public Game(final Long id, final String name, final List<String> images, final AlternativeGameNames alternativeGameNames) {
        this.id = id;
        this.name = name;
        this.images = images;
        this.alternativeGameNames = alternativeGameNames;
    }

    public int userHeadCount() {
        return rooms.totalHeadCount();
    }

    public void update(String name, List<String> alternativeNames, List<String> images) {
        this.name = name;
        this.alternativeGameNames.convertAndUpdateToGame(alternativeNames, this);
        this.images = images;
    }

    public void addRoom(final Room room) {
        rooms.addRoom(room);
    }

    public void addNames(final List<String> names) {
        validateToAddNames(names);

        for (String name : names) {
            addAlternativeName(new AlternativeGameName(name, this));
        }
    }

    private void validateToAddNames(final List<String> names) {
        Set<String> nameSet = new HashSet<>(names);
        if (nameSet.size() != names.size()) {
            throw new BabbleDuplicatedException(String.format("중복된 값이 있습니다.(%s)", Strings.join(names, ',')));
        }

        for (String name : names) {
            if (hasName(name)) {
                throw new BabbleDuplicatedException(String.format("이미 존재하는 이름 입니다.(%s)", name));
            }
        }
    }

    public void addAlternativeName(final AlternativeGameName alternativeGameName) {
        if (hasName(alternativeGameName.getValue())) {
            throw new BabbleDuplicatedException(String.format("이미 존재하는 이름 입니다.(%s)", alternativeGameName.getValue()));
        }

        alternativeGameNames.add(alternativeGameName);
    }

    public void removeAlternativeName(final AlternativeGameName alternativeGameName) {
        if (hasNotName(alternativeGameName.getValue())) {
            throw new BabbleNotFoundException(String.format("존재하지 않는 이름 입니다.(%s)", alternativeGameName.getValue()));
        }

        alternativeGameNames.remove(alternativeGameName);

        if (this.equals(alternativeGameName.getGame())) {
            alternativeGameName.delete();
        }
    }

    public boolean hasName(final String name) {
        return this.name.equals(name) || alternativeGameNames.contains(name);
    }

    public boolean hasNotName(final String name) {
        return !hasName(name);
    }

    public List<String> getAlternativeNames() {
        return alternativeGameNames.getNames();
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
