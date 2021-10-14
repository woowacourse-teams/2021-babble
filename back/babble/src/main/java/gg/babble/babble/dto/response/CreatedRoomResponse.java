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
public class CreatedRoomResponse {

    private Long roomId;
    private String createdDate;
    private GameResponse game;
    private int maxHeadCount;
    private List<TagResponse> tags;

    public static CreatedRoomResponse from(final Room room) {
        return CreatedRoomResponse.builder()
            .roomId(room.getId())
            .createdDate(room.getCreatedAt().toString())
            .game(GameResponse.from(room.getGame()))
            .maxHeadCount(room.maxHeadCount())
            .tags(tagResponses(room.getTagRegistrationsOfRoom()))
            .build();
    }

    private static List<TagResponse> tagResponses(TagRegistrationsOfRoom tagRegistrations) {
        return tagRegistrations.tags()
            .stream()
            .map(TagResponse::from)
            .collect(Collectors.toList());
    }
}
