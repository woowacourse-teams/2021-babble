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
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String category;
    private String nickname;
    private String createdAt;
    private String updatedAt;
    private Boolean notice;

    private Long view;
    private Long like;

    public static PostResponse from(final Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.category(), post.nickname(), post.createdAt(), post.updatedAt(),
            post.isNotice(), post.getView(), post.getLike());
    }
}
