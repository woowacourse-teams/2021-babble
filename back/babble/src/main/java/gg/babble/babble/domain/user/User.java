package gg.babble.babble.domain.user;

import gg.babble.babble.domain.Session;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
    @Embedded
    private Nickname nickname;

    @NotNull(message = "아바타는 Null 이어서는 안됩니다.")
    private String avatar;

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Session session;

    public User(final String nickname) {
        this(null, nickname);
    }

    public User(final Long id, final String nickname) {
        this.id = id;
        this.nickname = new Nickname(nickname);
        this.avatar = avatarByNickname(nickname);
    }

    public static String avatarByNickname(final String nickname) {
        long avatarIndex = ((long) nickname.hashCode() - Integer.MIN_VALUE) % NUMBER_OF_AVATAR;
        return String.format(AVATAR_FORMAT, avatarIndex);
    }

    public void linkSession(final Session session) {
        this.session = session;
    }

    public void unLinkSession(final Session session) {
        if (isNotLinkedSession(session)) {
            Long roomId = session.getRoom().getId();
            throw new BabbleIllegalArgumentException(String.format("[%d]번 유저는 [%d]번 방에 없으므로 나갈 수 없습니다.", id, roomId));
        }

        this.session = null;
        session.delete();
    }

    public String getNickname(){
        return nickname.getValue();
    }

    public boolean isLinkedSession(final Session session) {
        return !isNotLinkedSession(session);
    }

    private boolean isNotLinkedSession(final Session session) {
        return Objects.isNull(this.session) || !this.session.equals(session);
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
