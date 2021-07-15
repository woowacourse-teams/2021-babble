package gg.babble.babble.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequestDto {

    private Long gameId;
    private Long hostId;
    private List<String> tags;
}
