package gg.babble.babble.domain.room;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.domain.user.Users;
import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private Users users;

    @CreatedDate
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public Room(final Long id, @NonNull final Game game, @NonNull final List<Tag> tags, final LocalDateTime createdDate) {
        validate(tags);
        this.id = id;
        this.game = game;
        this.tagRegistrationsOfRoom = TagRegistrationsOfRoom.builder()
            .room(this)
            .tags(tags)
            .build();
        this.users = new Users();
        this.createdDate = createdDate;
        isDeleted = false;
    }

    private void validate(List<Tag> tags) {
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
        return users.first();
    }

    public List<User> getGuests() {
        return users.tail();
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }

    public boolean hasUser(User user) {
        return users.hasUser(user);
    }

    public boolean hasNotUser(final User user) {
        return !users.hasUser(user);
    }

    @Override
    public boolean equals(Object o) {
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
