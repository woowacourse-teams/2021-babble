package gg.babble.babble.dto.request;

import gg.babble.babble.domain.tag.AlternativeTagName;
import gg.babble.babble.domain.tag.Tag;
import java.util.List;

public class TagUpdateRequest {

    private String name;
    private List<String> alternativeNames;

    public TagUpdateRequest() {
    }

    public TagUpdateRequest(String name, List<String> alternativeNames) {
        this.name = name;
        this.alternativeNames = alternativeNames;
    }

    public Tag toEntity() {
        Tag tag = new Tag(name);
        alternativeNames.forEach(alternativeName -> new AlternativeTagName(alternativeName, tag));
        return tag;
    }
}
