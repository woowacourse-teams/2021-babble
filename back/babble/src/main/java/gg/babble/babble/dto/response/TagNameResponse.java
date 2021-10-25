package gg.babble.babble.dto.response;

import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.tag.TagName;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagNameResponse {

    private Long id;
    private String name;

    public static List<TagNameResponse> listFrom(final List<Tag> tags) {
        return tags.stream()
            .map(TagNameResponse::from)
            .collect(Collectors.toList());
    }

    public static TagNameResponse from(final Tag tag) {
        return new TagNameResponse(tag.getId(), tag.getName());
    }
}
