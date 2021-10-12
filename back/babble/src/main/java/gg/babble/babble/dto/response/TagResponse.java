package gg.babble.babble.dto.response;

import gg.babble.babble.domain.tag.AlternativeTagNames;
import gg.babble.babble.domain.tag.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagResponse {

    private final Long id;
    private final String name;
    private final List<AlternativeTagNameResponse> alternativeNames;

    public static TagResponse from(final Tag tag) {
        return new TagResponse(
            tag.getId(),
            tag.getName(),
            convertToResponse(tag.getAlternativeTagNames())
        );
    }

    private static List<AlternativeTagNameResponse> convertToResponse(AlternativeTagNames alternativeTagNames) {
        return alternativeTagNames.getElements()
            .stream()
            .map(AlternativeTagNameResponse::from)
            .collect(Collectors.toList());
    }
}
