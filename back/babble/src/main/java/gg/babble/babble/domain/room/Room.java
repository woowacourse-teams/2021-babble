package gg.babble.babble.domain.room;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.RoomUsers;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Embedded
    private TagRegistrationsOfRoom tagRegistrationsOfRoom;

    @Embedded
    private RoomUsers users;

    @CreatedDate
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private boolean isDeleted;

    public Room(final Long id, @NonNull final Game game, @NonNull final List<Tag> tags) {
        validateToConstruct(tags);
        this.id = id;
        this.game = game;
        this.users = new RoomUsers();
        this.isDeleted = false;
        this.tagRegistrationsOfRoom = new TagRegistrationsOfRoom(this, tags);
    }

    public Room(@NonNull final Game game, @NonNull final List<Tag> tags) {
        this(null, game, tags);
    }

    private static void validateToConstruct(final List<Tag> tags) {
        if (Objects.isNull(tags) || tags.isEmpty()) {
            throw new BabbleIllegalArgumentException("방의 태그는 1개 이상이어야 합니다.");
        }
    }

    public void join(final User user) {

        if (users.hasUser(user)) {
            throw new BabbleDuplicatedException("이미 해당 방에 참여 중입니다.");
        }

        users.add(user);

        if (user.hasNotRoom(this)) {
            user.join(this);
        }
    }

    public void leave(final User user) {
        validateToLeave(user);
        users.remove(user);
        delegateToLeave(user);

        if (users.isEmpty()) {
            isDeleted = true;
        }
    }

    private void delegateToLeave(final User user) {
        if (user.hasRoom(this)) {
            user.leave(this);
        }
    }

    private void validateToLeave(final User user) {
        if (hasNotUser(user)) {
            throw new BabbleNotFoundException("해당 방에 해당 유저가 존재하지 않습니다.");
        }
    }

    public User getHost() {
        return users.host();
    }

    public List<User> getGuests() {
        return users.guests();
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }

    public boolean hasUser(final User user) {
        return users.hasUser(user);
    }

    public boolean hasNotUser(final User user) {
        return !users.hasUser(user);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(id, room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
