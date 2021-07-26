package gg.babble.babble.domain.user;

import gg.babble.babble.domain.room.Room;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public User(@NonNull final String name, final Room room) {
        this(null, name, room);
    }

    public User(final Long id, @NonNull final String name) {
        this(id, name, null);
    }

    public User(@NonNull final String name) {
        this(null, name);
    }

    public void join(final Room room) {
        if (Objects.nonNull(this.room) && !this.room.equals(room)) {
            this.room.leave(this);
        }

        this.room = room;

        if (room.hasNotUser(this)) {
            this.room.join(this);
        }
    }

    public void leave(final Room room) {
        if (Objects.isNull(this.room) || !this.room.equals(room)) {
            throw new BabbleIllegalArgumentException("해당 방을 나갈 수 없습니다.");
        }

        this.room = null;
        delegateToLeave(room);
    }

    private void delegateToLeave(final Room room) {
        if (room.hasUser(this)) {
            room.leave(this);
        }
    }

    public boolean hasRoom(final Room room) {
        return !hasNotRoom(room);
    }

    public boolean hasNotRoom(final Room room) {
        return Objects.isNull(this.room) || !this.room.equals(room);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
