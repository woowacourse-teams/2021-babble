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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    private static final String AVATAR_FORMAT = "https://bucket-babble-front.s3.ap-northeast-2.amazonaws.com/img/users/profiles/profile%d.png";
    private static final int NUMBER_OF_AVATAR = 70;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "닉네임은 Null 이어서는 안됩니다.")
    private String nickname;

    @NotNull(message = "아바타는 Null 이어서는 안됩니다.")
    private String avatar;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private LocalDateTime joinedAt;

    /*
    TODO: User <-> Session <-> Room 연관관계 리팩토링 후 진행
    @Column(nullable = false)
    private boolean deleted = false;
    */

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
