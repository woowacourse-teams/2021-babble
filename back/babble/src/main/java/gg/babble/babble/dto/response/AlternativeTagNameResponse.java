package gg.babble.babble.dto.response;

import gg.babble.babble.domain.tag.AlternativeTagName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlternativeTagNameResponse {

    private Long id;
    private String name;

    public static AlternativeTagNameResponse from(AlternativeTagName alternativeTagName) {
        return new AlternativeTagNameResponse(
            alternativeTagName.getId(),
            alternativeTagName.getName()
        );
    }
}
