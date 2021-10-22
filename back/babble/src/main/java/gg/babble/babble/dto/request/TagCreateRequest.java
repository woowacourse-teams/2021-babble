package gg.babble.babble.dto.request;

import gg.babble.babble.domain.tag.Tag;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagCreateRequest {

    @NotNull
    private String name;
    @NotNull
    private List<String> alternativeNames;

    public Tag toEntity() {
        Tag tag = new Tag(name);
        tag.addNames(alternativeNames);

        return tag;
    }
}
