package gg.babble.babble.domain.user;

import gg.babble.babble.domain.room.Room;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User{

    @NotNull(message = "아바타는 Null 이어서는 안됩니다.")
    private final String avatar = "https://hyeon9mak.github.io/assets/images/9vatar.png";
    // TODO: 기본 경로 프론트에게 받아오기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "닉네임은 Null 이어서는 안됩니다.")
    private String nickname;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @LastModifiedDate
    private LocalDateTime joinedAt;

    public User(final String nickname) {
        this(null, nickname);
    }

    public User(final Long id, final String nickname) {
        this(id, nickname, null);
    }

    public User(final String nickname, final Room room) {
        this(null, nickname, room);
    }

    public User(final Long id, final String nickname, final Room room) {
        this.id = id;
        this.nickname = nickname;
        this.room = room;
    }

    public void join(final Room room) {
        if (Objects.nonNull(this.room) && !this.room.equals(room)) {
            this.room.leave(this);
        }

        joinedAt = LocalDateTime.now();
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
        this.joinedAt = null;
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
