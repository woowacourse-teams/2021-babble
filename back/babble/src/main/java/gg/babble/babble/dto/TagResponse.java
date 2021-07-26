package gg.babble.babble.dto;

import gg.babble.babble.domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {

    private String name;

    public static TagResponse from(final Tag tag) {
        return new TagResponse(tag.getName());
    }
}
