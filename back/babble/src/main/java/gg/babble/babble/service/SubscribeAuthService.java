package gg.babble.babble.service;

import gg.babble.babble.util.UrlPhaser;
import gg.babble.babble.exception.BabbleIllegalStatementException;
import org.springframework.stereotype.Service;

@Service
public class SubscribeAuthService {

    public static final String ROOM_UPDATE_SUBSCRIBE_PREFIX = "users";

    private final RoomService roomService;

    public SubscribeAuthService(final RoomService roomService) {
        this.roomService = roomService;
    }

    public void validate(final String destinationUrl) {
        if (destinationUrl.contains(ROOM_UPDATE_SUBSCRIBE_PREFIX)) {
            validateHeadCount(destinationUrl);
        }
    }

    private void validateHeadCount(final String destinationUrl) {
        Long roomId = UrlPhaser.getRoomId(destinationUrl);
        if (roomService.isFullRoom(roomId)) {
            throw new BabbleIllegalStatementException("유저가 가득찬 방에는 입장할 수 없습니다.");
        }
    }
}
