package gg.babble.babble.domain.board;

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
@SQLDelete(sql = "UPDATE board SET deleted = true WHERE id = ?")
@Where(clause = "deleted=false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Post post;

    @Embedded
    private Account account;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private final boolean deleted = false;

    public Board(final String title, final String content, final String category, final String nickname, final String password) {
        this(new Post(title, content), new Account(nickname, password), Category.of(category));
    }

    public Board(final Post post, final Account account, final Category category) {
        this.post = post;
        this.account = account;
        this.category = category;
    }

    public String title() {
        return post.getTitle();
    }

    public String content() {
        return post.getContent();
    }

    public String createdAt() {
        return createdAt.toString();
    }

    public String updatedAt() {
        return updatedAt.toString();
    }

    public Long view() {
        return post.getViewCount();
    }

    public Long like() {
        return post.getLikeCount();
    }

    public String category() {
        return category.getName();
    }

    public String nickname() {
        return account.getNickname();
    }

    public void update(final String title, final String content, final String category, final String password) {
        validatePassword(password);
        post.update(title, content);
        this.category = Category.of(category);
    }

    public void delete(final String password) {
        validatePassword(password);
    }

    private void validatePassword(final String password) {
        if (account.isWrongPassword(password)) {
            throw new BabbleIllegalArgumentException("올바르지 않은 비밀번호가 입력 되었습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Board board = (Board) o;
        return Objects.equals(id, board.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isNotice() {
        return category.isNotice();
    }
}
