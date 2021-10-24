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

    private List<PostWithoutContentResponse> results;
    private String keyword;
    private String type;

    public static PostSearchResponse from(final List<PostWithoutContentResponse> postResponses, final String keyword, final String type) {
        return new PostSearchResponse(postResponses, keyword, type);
    }
}
