package gg.babble.babble.dto.response;

import gg.babble.babble.domain.post.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostBaseResponse {

    private Post post;
    private List<Post> posts;
    private String keyword;
    private String type;

    public static PostBaseResponse from(final Post post) {
        return new PostBaseResponse(post, null, null, null);
    }

    public static PostBaseResponse from(final List<Post> posts) {
        return new PostBaseResponse(null, posts, null, null);
    }

    public static PostBaseResponse of(final List<Post> posts, final String keyword, final String type) {
        return new PostBaseResponse(null, posts, keyword, type);
    }

    public PostSearchResponse toPostSearchResponse() {
        return PostSearchResponse.from(toPostWithoutContentResponse(), keyword, type);
    }

    public PostResponse toPostResponse() {
        return PostResponse.from(post);
    }

    public List<PostWithoutContentResponse> toPostWithoutContentResponse() {
        return posts.stream()
            .map(PostWithoutContentResponse::from)
            .collect(Collectors.toList());
    }
}
