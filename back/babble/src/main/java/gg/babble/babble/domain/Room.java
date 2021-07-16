package gg.babble.babble.domain;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleNotFoundException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User host;

    @NonNull
    @ManyToMany
    @JoinTable(name = "room_tag",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_name"))
    private List<Tag> tags;

    @OneToMany(mappedBy = "room")
    private List<User> guests;

    @CreatedDate
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public Room(final Long id, @NonNull final Game game, final User host, @NonNull final List<Tag> tags,
                LocalDateTime createdDate) {
        this.id = id;
        this.game = game;
        this.host = host;
        this.tags = tags;
        this.createdDate = createdDate;
        this.guests = new ArrayList<>();
        isDeleted = false;
    }

    public void join(final User user) {
        if (hasUser(user)) {
            throw new BabbleDuplicatedException("이미 해당 방에 참여 중입니다.");
        }

        if (isEmptyHost()) {
            host = user;
        } else {
            guests.add(user);
        }

        if (user.hasNotRoom(this)) {
            user.join(this);
        }
    }

    private boolean isEmptyHost() {
        return Objects.isNull(host);
    }

    public void leave(final User user) {
        validateToLeave(user);

        if (isDeletable(user)) {
            delete();
            return;
        }
        if (host.equals(user)) {
            delegateHost();
        }

        guests.remove(user);
        delegateToLeave(user);
    }

    private void delete() {
        isDeleted = true;
    }

    private void delegateToLeave(final User user) {
        if (user.hasRoom(this)) {
            user.leave(this);
        }
    }

    private void validateToLeave(final User user) {
        if (hasNotUser(user)) {
            throw new BabbleNotFoundException("해당 방에 해당 유저가 존재하지 않습니다.");
        }
    }

    private void delegateHost() {
        User hostToLeave = host;

        host = guests.get(0);
        guests.remove(host);

        delegateToLeave(hostToLeave);
    }

    private boolean isDeletable(final User user) {
        return host.equals(user) && guests.isEmpty();
    }

    public boolean hasUser(final User user) {
        return (Objects.nonNull(host) && host.equals(user)) || guests.contains(user);
    }

    public boolean hasNotUser(final User user) {
        return !hasUser(user);
    }
}
