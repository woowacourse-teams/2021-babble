package gg.babble.babble.domain;

import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.user.User;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "세션 Id는 Null 일 수 없습니다.")
    private String sessionId;

    @NotNull(message = "방은 Null 일 수 없습니다.")
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @NotNull(message = "유저는 Null 일 수 없습니다.")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    private LocalDateTime createdDate;

    // TODO: User <-> Session <-> Room 연관관계 리팩토링 후 진행
    @Column(nullable = false)
    private boolean deleted = false;

    public Session(final String sessionId, final Room room, final User user) {
        this(null, sessionId, room, user, LocalDateTime.now());
    }

    public Session(final Long id, final String sessionId, final Room room, final User user, final LocalDateTime createdDate) {
        this.id = id;
        this.sessionId = sessionId;
        this.room = room;
        this.user = user;
        this.createdDate = createdDate;
    }

    public void userEnterRoom() {
        this.room.enterSession(this);
        this.user.linkSession(this);
    }

    public void userExitRoom() {
        room.exitSession(this);
        user.unlinkSession(this);
        deleted = true;
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
