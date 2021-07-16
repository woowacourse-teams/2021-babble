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
    @ManyToMany
    @JoinTable(name = "room_tag",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_name"))
    private List<Tag> tags;

    @OneToMany(mappedBy = "room")
    private final List<User> users = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public Room(final Long id, @NonNull final Game game, @NonNull final List<Tag> tags, LocalDateTime createdDate) {
        this.id = id;
        this.game = game;
        this.tags = tags;
        this.createdDate = createdDate;
        isDeleted = false;
    }

    public void join(final User user) {

        if (hasUser(user)) {
            throw new BabbleDuplicatedException("이미 해당 방에 참여 중입니다.");
        }

        users.add(user);

        if (user.hasNotRoom(this)) {
            user.join(this);
        }
    }

    public void leave(final User user) {
        validateToLeave(user);
        users.remove(user);
        delegateToLeave(user);

        if (users.isEmpty()) {
            isDeleted = true;
        }
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

    public User getHost() {
        return users.get(0);
    }

    public List<User> getGuests() {
        return users.subList(1, users.size());
    }

    public boolean hasUser(final User user) {
        return users.contains(user);
    }

    public boolean hasNotUser(final User user) {
        return !hasUser(user);
    }
}
