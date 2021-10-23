package gg.babble.babble.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchResponse {

    private List<PostResponse> results;
    private String keyword;
    private String type;

    public static PostSearchResponse from(final List<PostResponse> responses, final String keyword, final String type) {
        return new PostSearchResponse(responses, keyword, type);
    }
}
