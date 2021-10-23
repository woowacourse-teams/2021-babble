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

    @Column(nullable = false)
    private final boolean deleted = false;

    public Post(final String title, final String content, final String category, final String nickname, final String password) {
        this(title, content, Category.from(category), new Account(nickname, password));
    }

    public Post(final String title, final String content, final Category category, final Account account) {
        this.title = title;
        this.content = content;
        this.account = account;
        this.category = category;
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
        validatePassword(password);
        this.title = title;
        this.content = content;
        this.category = Category.from(category);
    }

    public void delete(final String password) {
        validatePassword(password);
    }

    private void validatePassword(final String password) {
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
