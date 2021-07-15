package gg.babble.babble.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequestDto {

    private Long gameId;
    private Long hostId;
    private List<String> tags;
}
