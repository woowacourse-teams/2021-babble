package gg.babble.babble.dto;

import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.room.TagRegistrationsOfRoom;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class FoundRoomResponse {

    private final Long roomId;
    private final String createdDate;
    private final GameResponse game;
    private final UserResponse host;
    private final HeadCountResponse headCount;
    private final List<TagResponse> tags;

    public static FoundRoomResponse from(final Room room) {
        return FoundRoomResponse.builder()
            .roomId(room.getId())
            .createdDate(room.getCreatedDate().toString())
            .game(GameResponse.from(room.getGame()))
            .host(UserResponse.from(room.getHost()))
            .headCount(HeadCountResponse.from(room))
            .tags(tagResponses(room.getTagRegistrationsOfRoom()))
            .build();
    }

    private static List<TagResponse> tagResponses(final TagRegistrationsOfRoom tagRegistrations) {
        return tagRegistrations.tagNames()
            .stream()
            .map(TagResponse::new)
            .collect(Collectors.toList());
    }
}
