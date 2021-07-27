package gg.babble.babble.domain.room;

import gg.babble.babble.exception.BabbleIllegalArgumentException;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MaxHeadCount {

    private static final int MIN_VALUE = 2;
    private static final int MAX_VALUE = 20;
    private int value;

    public MaxHeadCount(final int value) {
        validateToConstruct(value);
        this.value = value;
    }

    private static void validateToConstruct(final int value) {
        if (value < MIN_VALUE || MAX_VALUE < value) {
            throw new BabbleIllegalArgumentException(String.format("방 참가인원은 %d명에서 %d명 사이여야 합니다.", MIN_VALUE, MAX_VALUE));
        }
    }
}
