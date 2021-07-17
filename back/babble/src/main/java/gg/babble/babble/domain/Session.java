package gg.babble.babble.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String sessionId;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @NonNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Session(final Long id, @NonNull final String sessionId, @NonNull final Room room, @NonNull final User user) {
        this.id = id;
        this.sessionId = sessionId;
        this.room = room;
        this.user = user;
    }
}