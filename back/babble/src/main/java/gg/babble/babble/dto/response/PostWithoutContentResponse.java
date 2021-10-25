package gg.babble.babble.dto.response;

import gg.babble.babble.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostWithoutContentResponse {

    private Long id;
    private String title;
    private String category;
    private String nickname;
    private String createdAt;
    private String updatedAt;
    private Boolean notice;

    private Long view;
    private Long like;

    public static PostWithoutContentResponse from(final Post post) {
        return new PostWithoutContentResponse(post.getId(), post.getTitle(), post.category(), post.nickname(), post.createdAt(), post.updatedAt(),
            post.isNotice(), post.getView(), post.getLike());
    }
}
