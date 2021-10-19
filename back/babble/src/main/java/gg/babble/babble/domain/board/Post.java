package gg.babble.babble.domain.board;

import java.time.LocalDateTime;
import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Embeddable
@NoArgsConstructor
public class Post {

    private String title;
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private Long view;
    private Long like;

    public Post(final String title, final String content) {
        this.title = title;
        this.content = content;
    }

    public void update(final String title, final String content) {
        this.title = title;
        this.content = content;
        updatedAt = LocalDateTime.now();
    }
}
