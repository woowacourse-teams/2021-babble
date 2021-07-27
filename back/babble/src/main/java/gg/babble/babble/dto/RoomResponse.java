package gg.babble.babble.dto;

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
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {

    private Long roomId;
    private String createdDate;
    private GameResponse game;
    private UserResponse host;
    private HeadCountResponse headCount;
    private List<TagResponse> tags;

    public static RoomResponse from(final Room room) {
        return RoomResponse.builder()
            .roomId(room.getId())
            .createdDate(room.getCreatedDate().toString())
            .game(GameResponse.from(room.getGame()))
            .host(UserResponse.from(room.getHost()))
            .headCount(HeadCountResponse.from(room))
            .tags(tagResponses(room.getTagRegistrationsOfRoom()))
            .build();
    }

    private static List<TagResponse> tagResponses(TagRegistrationsOfRoom tagRegistrations) {
        return tagRegistrations.tagNames()
            .stream()
            .map(TagResponse::new)
            .collect(Collectors.toList());
    }
}
