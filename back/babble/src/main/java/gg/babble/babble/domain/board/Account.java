package gg.babble.babble.domain.board;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Account {

    public static final int MIN_PASSWORD_LENGTH = 4;
    public static final int MAX_PASSWORD_LENGTH = 20;

    private String nickname;
    private String password;

    public Account(final String nickname, final String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public boolean isWrongPassword(final String password) {
        return !this.password.equals(password);
    }
}
