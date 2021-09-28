package gg.babble.babble.dto.request;

import gg.babble.babble.domain.game.AlternativeGameName;
import gg.babble.babble.domain.game.Game;
import java.util.List;
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
    private List<String> images;

    @NotNull(message = "대안 이름은 Null 일 수 없습니다.")
    private List<String> alternativeNames;

    public Game toEntity() {
        final Game game = new Game(name, images);
        alternativeNames.forEach(alternativeName -> new AlternativeGameName(alternativeName, game));
        return game;
    }
}
