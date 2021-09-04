package gg.babble.babble.dto.response;

import gg.babble.babble.domain.tag.Tag;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagResponse {

    private final Long id;
    private final String name;
    private final Set<String> alternativeNames;

    public static TagResponse from(final Tag tag) {
        return new TagResponse(tag.getId(), tag.getName(), tag.getAlternativeTagNames().getNames());
    }
}
