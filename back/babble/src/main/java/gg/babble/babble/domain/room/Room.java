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
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @NotNull(message = "게임은 Null 이어서는 안됩니다.")
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Embedded
    private TagRegistrationsOfRoom tagRegistrationsOfRoom;

    @Embedded
    private RoomUsers users;

    @Embedded
    private MaxHeadCount maxHeadCount;

    @CreatedDate
    private LocalDateTime createdDate;

    // TODO: User <-> Session <-> Room 연관관계 리팩토링 후 진행
    @Column(nullable = false)
    private boolean deleted = false;

    public Room(final Game game, final List<Tag> tags, final MaxHeadCount maxHeadCount) {
        this(null, game, tags, maxHeadCount);
    }

    public Room(final Long id, final Game game, final List<Tag> tags, final MaxHeadCount maxHeadCount) {
        validateToConstruct(tags);
        this.id = id;
        this.game = game;
        this.users = new RoomUsers();
        this.tagRegistrationsOfRoom = new TagRegistrationsOfRoom(this, tags);
        this.maxHeadCount = maxHeadCount;
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
            deleted = true;
        }
    }

    private void validateToLeave(final User user) {
        if (hasNotUser(user)) {
            throw new BabbleNotFoundException("해당 방에 해당 유저가 존재하지 않습니다.");
        }
    }

    private void delegateToLeave(final User user) {
        if (user.hasRoom(this)) {
            user.leave(this);
        }
    }

    public int currentHeadCount() {
        return users.headCount();
    }

    public int maxHeadCount() {
        return maxHeadCount.getValue();
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

    public boolean isFull() {
        return maxHeadCount() == currentHeadCount();
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
