package gg.babble.babble.domain.board;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Post {

    private String title;
    private String content;

    private Long viewCount = 0L;
    private Long likeCount = 0L;

    public Post(final String title, final String content) {
        this.title = title;
        this.content = content;
    }

    public void update(final String title, final String content) {
        this.title = title;
        this.content = content;
    }
}
