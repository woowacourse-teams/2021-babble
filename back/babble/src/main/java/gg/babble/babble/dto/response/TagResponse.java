package gg.babble.babble.dto.response;

import gg.babble.babble.domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {

    private Long id;
    private String name;

    public static TagResponse from(final Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}
