package gg.babble.babble.domain.room;

import gg.babble.babble.exception.BabbleIllegalArgumentException;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MaxHeadCount {

    private static final int MIN_VALUE = 2;
    private int value;

    public MaxHeadCount(final int value) {
        validateToConstruct(value);
        this.value = value;
    }

    private static void validateToConstruct(final int value) {
        if (value < MIN_VALUE) {
            throw new BabbleIllegalArgumentException("방 최대 참가 인원 최소 2인 이상이어야 합니다.");
        }
    }
}
