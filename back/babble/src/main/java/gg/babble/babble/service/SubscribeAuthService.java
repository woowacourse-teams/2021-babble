package gg.babble.babble.service;

import gg.babble.babble.exception.BabbleIllegalStatementException;
import gg.babble.babble.util.UrlParser;
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
        Long roomId = UrlParser.getRoomId(destinationUrl);
        if (roomService.isFullRoom(roomId)) {
            throw new BabbleIllegalStatementException(String.format("%d번방은 가득차 있어 입장할 수 없습니다.", roomId));
        }
    }
}
