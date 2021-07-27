package gg.babble.babble.dto;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {

    @NotNull(message = "게임 ID는 비어있을 수 없습니다.")
    @Positive(message = "게임 ID는 1보다 큰 수여야 합니다.")
    private Long gameId;
    @NotNull(message = "태그 목록은 Null 일 수 없습니다.")
    private List<TagRequest> tags;
    @Min(value = 2, message = "방 최대 참가 인원 최소 2인 이상이어야 합니다.")
    private int maxHeadCount;
}
