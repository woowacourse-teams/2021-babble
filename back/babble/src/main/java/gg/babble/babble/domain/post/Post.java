package gg.babble.babble.domain.post;

import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@SQLDelete(sql = "UPDATE post SET deleted = true WHERE id = ?")
@Where(clause = "deleted=false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post {

    private static final int MAX_CONTENT_BYTES = 8000;
    private static final int MAX_TITLE_LENGTH = 50;
    @Column(nullable = false)
    private final boolean deleted = false;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @Column(name = "view_count")
    private Long view = 0L;
    @Column(name = "like_count")
    private Long like = 0L;
    @Enumerated(value = EnumType.STRING)
    private Category category;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Embedded
    private Account account;

    public Post(final String title, final String content, final String category, final String nickname, final String password) {
        this(title, content, Category.getCategoryByName(category), new Account(nickname, password));
    }

    public Post(final String title, final String content, final Category category, final Account account) {
        validateTitleSize(title);
        validateContentSize(content);
        this.title = title;
        this.content = content;
        this.account = account;
        this.category = category;
    }

    private static void validateTitleSize(final String title) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new BabbleIllegalArgumentException(String.format("제목은 %d글자까지만 작성할 수 있습니다.(현재 글자 수: %d)", MAX_TITLE_LENGTH, title.length()));
        }
    }

    private static void validateContentSize(final String content) {
        int byteSize = content.getBytes().length;
        if (byteSize > MAX_CONTENT_BYTES) {
            throw new BabbleIllegalArgumentException(String.format("게시글은 %dbytes까지만 작성할 수 있습니다.(현재 bytes: %d)", MAX_CONTENT_BYTES, byteSize));
        }
    }

    public String createdAt() {
        return createdAt.toString();
    }

    public String updatedAt() {
        return updatedAt.toString();
    }

    public String category() {
        return category.getName();
    }

    public String nickname() {
        return account.getNickname();
    }

    public void update(final String title, final String content, final String category, final String password) {
        validateTitleSize(title);
        validateContentSize(content);
        validatePassword(password);
        this.title = title;
        this.content = content;
        this.category = Category.getCategoryByName(category);
    }

    public void validatePassword(final String password) {
        if (account.isWrongPassword(password)) {
            throw new BabbleIllegalArgumentException("올바르지 않은 비밀번호가 입력 되었습니다.");
        }
    }

    public boolean isNotice() {
        return category.isNotice();
    }

    public void addView() {
        view += 1;
    }

    public void addLike() {
        like += 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
