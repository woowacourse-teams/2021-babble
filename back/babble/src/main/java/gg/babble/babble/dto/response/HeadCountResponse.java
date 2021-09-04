package gg.babble.babble.dto.response;

import gg.babble.babble.domain.room.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class HeadCountResponse {

    private final int current;
    private final int max;

    public static HeadCountResponse from(final Room room) {
        return new HeadCountResponse(room.currentHeadCount(), room.maxHeadCount());
    }
}
