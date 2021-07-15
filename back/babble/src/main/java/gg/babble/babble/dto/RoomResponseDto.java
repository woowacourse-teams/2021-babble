package gg.babble.babble.dto;

import gg.babble.babble.domain.Room;
import gg.babble.babble.domain.Tag;
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
public class RoomResponseDto {

    private Long roomId;
    private String createdDate;
    private GameResponseDto game;
    private HostResponseDto host;
    private List<String> tags;

    public static RoomResponseDto from(Room room) {
        return RoomResponseDto.builder()
            .roomId(room.getId())
            .createdDate(room.getCreatedDate().toString())
            .game(GameResponseDto.from(room.getGame()))
            .host(HostResponseDto.from(room.getHost()))
            .tags(tagNames(room.getTags()))
            .build();
    }

    private static List<String> tagNames(List<Tag> tags) {
        return tags.stream()
            .map(Tag::getName)
            .collect(Collectors.toList());
    }
}
