package gg.babble.babble.domain.user;

import gg.babble.babble.domain.Session;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    private static final String AVATAR_FORMAT = "https://d2bidcnq0n74fu.cloudfront.net/img/users/profiles/profile%d.png";
    private static final int NUMBER_OF_AVATAR = 70;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "닉네임은 Null 이어서는 안됩니다.")
    @Embedded
    private Nickname nickname;

    @NotNull(message = "아바타는 Null 이어서는 안됩니다.")
    private String avatar;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Session session;

    private final boolean deleted = false;

    public User(final String nickname) {
        this(null, nickname);
    }

    public User(final Long id, final String nickname) {
        this.id = id;
        this.nickname = new Nickname(nickname);
        this.avatar = avatarByNickname(nickname);
    }

    private static String avatarByNickname(final String nickname) {
        long avatarIndex = ((long) nickname.hashCode() - Integer.MIN_VALUE) % NUMBER_OF_AVATAR;
        return String.format(AVATAR_FORMAT, avatarIndex);
    }

    public void linkSession(final Session session) {
        this.session = session;
    }

    public String getNickname() {
        return nickname.getValue();
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
