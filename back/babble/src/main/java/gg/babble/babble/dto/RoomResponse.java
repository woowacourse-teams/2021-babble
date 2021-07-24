package gg.babble.babble.dto;

import gg.babble.babble.domain.Room;
import gg.babble.babble.domain.Tag;
import gg.babble.babble.domain.TagRegistration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {

    private Long roomId;
    private String createdDate;
    private GameResponse game;
    private List<String> tags;

    public static RoomResponse from(final Room room) {
        return RoomResponse.builder()
                .roomId(room.getId())
                .createdDate(room.getCreatedDate().toString())
                .game(GameResponse.from(room.getGame()))
                .tags(tagNames(room.getTagRegistrations()))
                .build();
    }

    private static List<String> tagNames(List<TagRegistration> tagRegistrations) {
        return tagRegistrations.stream()
            .map(tagRegistration -> tagRegistration.getTag().getName())
            .collect(Collectors.toList());
    }
}
