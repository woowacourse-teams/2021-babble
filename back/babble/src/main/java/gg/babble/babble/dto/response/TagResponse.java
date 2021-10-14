package gg.babble.babble.dto.response;

import gg.babble.babble.domain.tag.AlternativeTagNames;
import gg.babble.babble.domain.tag.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {

    private Long id;
    private String name;
    private List<AlternativeTagNameResponse> alternativeNames;

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
