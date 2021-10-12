package gg.babble.babble.dto.response;

import gg.babble.babble.domain.tag.AlternativeTagName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlternativeTagNameResponse {

    private final Long id;
    private final String name;

    public static AlternativeTagNameResponse from(AlternativeTagName alternativeTagName) {
        return new AlternativeTagNameResponse(
            alternativeTagName.getId(),
            alternativeTagName.getName()
        );
    }
}
