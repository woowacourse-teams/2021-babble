package gg.babble.babble.domain.room;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
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
    private final Sessions sessions = new Sessions();

    @Embedded
    private MaxHeadCount maxHeadCount;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean deleted = false;

    public Room(final Game game, final List<Tag> tags, final MaxHeadCount maxHeadCount) {
        this(null, game, tags, maxHeadCount);
    }

    public Room(final Long id, final Game game, final List<Tag> tags, final MaxHeadCount maxHeadCount) {
        validateToConstruct(tags);
        this.id = id;
        this.game = game;
        this.tagRegistrationsOfRoom = new TagRegistrationsOfRoom(this, tags);
        this.maxHeadCount = maxHeadCount;

        game.addRoom(this);
    }

    private static void validateToConstruct(final List<Tag> tags) {
        if (Objects.isNull(tags) || tags.isEmpty()) {
            throw new BabbleIllegalArgumentException("방의 태그가 입력되지 않았습니다.");
        }
    }

    public void enterSession(final Session session) {
        if (sessions.contains(session)) {
            throw new BabbleDuplicatedException(String.format("[%d] 유저는 이미 [%d] 방에 참여 중이라 입장이 불가능 합니다.", session.getUserId(), id));
        }

        sessions.enter(session);
    }

    public void exitSession(final Session session) {
        if (sessions.noContains(session)) {
            throw new BabbleNotFoundException(String.format("[%d] 유저는 [%d] 방에 참여하지 않아 퇴장이 불가능 합니다.", session.getUserId(), id));
        }

        sessions.exit(session);

        if (sessions.isEmpty()) {
            deleted = true;
        }
    }

    public int currentHeadCount() {
        return sessions.headCount();
    }

    public int maxHeadCount() {
        return maxHeadCount.getValue();
    }

    public User getHost() {
        List<User> users = sessions.sortedUsersByEnteredTime();

        return users.get(0);
    }

    public List<User> getGuests() {
        List<User> users = sessions.sortedUsersByEnteredTime();

        return users.subList(1, users.size());
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
