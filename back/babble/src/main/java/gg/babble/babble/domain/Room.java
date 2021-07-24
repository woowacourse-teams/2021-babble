package gg.babble.babble.domain;

import gg.babble.babble.domain.user.User;
import gg.babble.babble.domain.user.Users;
import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private final List<TagRegistration> tagRegistrations = new ArrayList<>();

    @Embedded
    private Users users;

    @CreatedDate
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public Room(final Long id, @NonNull final Game game, @NonNull final List<Tag> tags, final LocalDateTime createdDate) {
        validate(tags);
        this.id = id;
        this.game = game;
        this.tagRegistrations.addAll(tagRegistrationsFromTag(tags));
        this.users = new Users();
        this.createdDate = createdDate;
        isDeleted = false;
    }

    private void validate(List<Tag> tags) {
        if (Objects.isNull(tags) || tags.isEmpty()) {
            throw new BabbleIllegalArgumentException("방의 태그는 1개 이상이어야 합니다.");
        }
    }

    private List<TagRegistration> tagRegistrationsFromTag(List<Tag> tags) {
        return tags.stream()
            .map(tag -> TagRegistration.builder()
                .room(this)
                .tag(tag)
                .build())
            .collect(Collectors.toList());
    }

    public void join(final User user) {

        if (users.hasUser(user)) {
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
        return users.first();
    }

    public List<User> getGuests() {
        return users.tail();
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }

    public boolean hasUser(User user) {
        return users.hasUser(user);
    }

    public boolean hasNotUser(final User user) {
        return !users.hasUser(user);
    }
}
