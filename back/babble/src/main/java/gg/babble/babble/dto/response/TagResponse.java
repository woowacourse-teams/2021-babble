package gg.babble.babble.dto.response;

import gg.babble.babble.domain.tag.Tag;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {

    private Long id;
    private String name;
    private Set<String> alternativeNames;

    public static TagResponse from(final Tag tag) {
        return new TagResponse(tag.getId(), tag.getName(), tag.getAlternativeTagNames().getNames());
    }
}
