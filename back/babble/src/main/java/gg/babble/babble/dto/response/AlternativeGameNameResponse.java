package gg.babble.babble.dto.response;

import gg.babble.babble.domain.game.AlternativeGameName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlternativeGameNameResponse {

    private final Long id;
    private final String name;

    public static AlternativeGameNameResponse from(AlternativeGameName alternativeGameName) {
        return new AlternativeGameNameResponse(
            alternativeGameName.getId(),
            alternativeGameName.getValue()
        );
    }
}
