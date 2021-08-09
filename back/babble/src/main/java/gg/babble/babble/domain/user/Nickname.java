package gg.babble.babble.domain.user;

import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Nickname {

    public static final int MIN_NICKNAME_LENGTH = 1;
    public static final int MAX_NICKNAME_LENGTH = 24;

    public static final String NICKNAME_REGEXP = "[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\s]+";
    private static final Pattern NICKNAME_PATTERN = Pattern.compile(NICKNAME_REGEXP);

    @Column(name = "nickname")
    private String value;

    public Nickname(String value) {
        value = value.trim();
        validateToConstruct(value);
        this.value = value;
    }

    private void validateToConstruct(final String value) {
        if (value.length() < MIN_NICKNAME_LENGTH || value.length() > MAX_NICKNAME_LENGTH) {
            throw new BabbleIllegalArgumentException(
                String.format("유저 닉네임은 %d자 이상 %d자 이하입니다. 현재 닉네임 길이: %d", MIN_NICKNAME_LENGTH, MAX_NICKNAME_LENGTH, value.length())
            );
        }
        if (!NICKNAME_PATTERN.matcher(value).matches()) {
            throw new BabbleIllegalArgumentException(String.format("닉네임은 한글, 영어, 숫자, 공백만 포함 가능합니다. 현재 닉네임: %s", value));
        }
    }
}
