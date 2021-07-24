package gg.babble.babble.dto;

import gg.babble.babble.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {
    private String name;

    public static TagResponse from(final Tag tag) {
        return TagResponse.builder()
                .name(tag.getName())
                .build();
    }
}
