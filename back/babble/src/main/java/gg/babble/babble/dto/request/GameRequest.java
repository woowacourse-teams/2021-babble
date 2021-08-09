package gg.babble.babble.dto.request;

import gg.babble.babble.domain.Game;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameRequest {

    @NotNull(message = "게임 이름은 Null 일 수 없습니다.")
    private String name;

    @NotNull(message = "게임 썸네일 경로는 Null 일 수 없습니다.")
    private String thumbnail;

    public Game toEntity() {
        return new Game(name, thumbnail);
    }
}
