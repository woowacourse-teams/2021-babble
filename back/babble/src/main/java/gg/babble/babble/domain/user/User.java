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
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    private static final String AVATAR_FORMAT = "https://bucket-babble-front.s3.ap-northeast-2.amazonaws.com/img/users/profiles/profile%d.png";
    private static final int NUMBER_OF_AVATAR = 70;
    public static final int MIN_NICKNAME_LENGTH = 1;
    public static final int MAX_NICKNAME_LENGTH = 24;

    @NotNull(message = "닉네임은 Null 이어서는 안됩니다.")
    @Size(min = MIN_NICKNAME_LENGTH, max = MAX_NICKNAME_LENGTH)
    private String nickname;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @NotNull(message = "아바타는 Null 이어서는 안됩니다.")
    private String avatar;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
        this.avatar = avatarByNickname(nickname);
    }

    public static String avatarByNickname(final String nickname) {
        long avatarIndex = ((long) nickname.hashCode() - Integer.MIN_VALUE) % NUMBER_OF_AVATAR;
        return String.format(AVATAR_FORMAT, avatarIndex);
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
            throw new BabbleIllegalArgumentException(String.format("%s 방에 %s 유저가 존재하지 않습니다.", room.getId(), id));
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
