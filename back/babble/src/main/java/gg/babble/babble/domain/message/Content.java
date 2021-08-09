package gg.babble.babble.domain.message;

import gg.babble.babble.exception.BabbleLengthException;
import lombok.Getter;

@Getter
public class Content {

    public static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 300;
    private final String value;

    public Content(final String value) {
        validateContent(value);
        this.value = value;
    }

    private static void validateContent(final String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new BabbleLengthException(
                String.format("메시지는 %d자 이상 %d자 이하 입니다. 현재 길이: (%d)", MIN_LENGTH, MAX_LENGTH, value.length())
            );
        }
    }
}
