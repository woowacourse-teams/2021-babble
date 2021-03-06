package gg.babble.babble.dto.response;

import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.room.TagRegistrationsOfRoom;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FoundRoomResponse {

    private Long roomId;
    private String createdDate;
    private GameResponse game;
    private UserResponse host;
    private HeadCountResponse headCount;
    private List<TagResponse> tags;

    public static FoundRoomResponse from(final Room room) {
        return FoundRoomResponse.builder()
            .roomId(room.getId())
            .createdDate(room.getCreatedAt().toString())
            .game(GameResponse.from(room.getGame()))
            .host(UserResponse.from(room.getHost()))
            .headCount(HeadCountResponse.from(room))
            .tags(tagResponses(room.getTagRegistrationsOfRoom()))
            .build();
    }

    private static List<TagResponse> tagResponses(final TagRegistrationsOfRoom tagRegistrations) {
        return tagRegistrations.tags()
            .stream()
            .map(TagResponse::from)
            .collect(Collectors.toList());
    }
}
