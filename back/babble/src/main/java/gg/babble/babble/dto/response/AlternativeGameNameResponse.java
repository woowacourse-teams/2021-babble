package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.AlternativeGameName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlternativeGameNameResponse {

    private Long id;
    private String name;

    public static AlternativeGameNameResponse from(AlternativeGameName alternativeGameName) {
        return new AlternativeGameNameResponse(
            alternativeGameName.getId(),
            alternativeGameName.getValue()
        );
    }
}
