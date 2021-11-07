package gg.babble.babble.domain;

import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.user.User;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@SQLDelete(sql = "UPDATE session SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "세션 Id는 Null 일 수 없습니다.")
    private String sessionId;
    @NotNull(message = "유저는 Null 일 수 없습니다.")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @NotNull(message = "방은 Null 일 수 없습니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    private final boolean deleted = false;

    public Session(final String sessionId, final User user, final Room room) {
        this(null, sessionId, user, room);
    }

    public Session(final Long id, final String sessionId, final User user, final Room room) {
        this.id = id;
        this.sessionId = sessionId;
        this.user = user;
        this.room = room;
        this.createdAt = LocalDateTime.now();

        user.linkSession(this);
        room.enterSession(this);
    }

    public Long getUserId() {
        return user.getId();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return Objects.equals(id, session.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
