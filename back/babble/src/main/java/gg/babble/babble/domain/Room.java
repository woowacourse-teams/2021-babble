package gg.babble.babble.domain;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @NonNull
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
    public Room(Long id, @NonNull Game game, @NonNull User host, @NonNull List<Tag> tags,
        LocalDateTime createdDate) {
        this.id = id;
        this.game = game;
        this.host = host;
        this.tags = tags;
        this.createdDate = createdDate;
        this.guests = new ArrayList<>();
        host.join(this);
        isDeleted = false;
    }

    public void join(User user) {
        if (hasUser(user)) {
            throw new BabbleDuplicatedException("이미 해당 방에 참여 중입니다.");
        }

        guests.add(user);

        if (user.hasNotRoom(this)) {
            user.join(this);
        }
    }

    public void leave(User user) {
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

    private void delegateToLeave(User user) {
        if (user.hasRoom(this)) {
            user.leave(this);
        }
    }

    private void validateToLeave(User user) {
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

    private boolean isDeletable(User user) {
        return host.equals(user) && guests.isEmpty();
    }

    public boolean hasUser(User user) {
        return (Objects.nonNull(host) && host.equals(user)) || guests.contains(user);
    }

    public boolean hasNotUser(User user) {
        return !hasUser(user);
    }
}
