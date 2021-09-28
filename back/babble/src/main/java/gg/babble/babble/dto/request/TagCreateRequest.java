package gg.babble.babble.dto.request;

import gg.babble.babble.domain.tag.AlternativeTagName;
import gg.babble.babble.domain.tag.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagCreateRequest {

    private String name;
    private List<String> alternativeNames;

    public Tag toEntity() {
        Tag tag = new Tag(name);
        alternativeNames.forEach(alternativeName -> new AlternativeTagName(alternativeName, tag));
        return tag;
    }
}
