package gg.babble.babble.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {

    @NotNull(message = "게임 ID는 비어있을 수 없습니다.")
    @Positive(message = "게임 ID는 1보다 큰 수여야 합니다.")
    private Long gameId;
    @NotNull(message = "태그 목록은 Null 일 수 없습니다.")
    private List<String> tags;
}
