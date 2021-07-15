package gg.babble.babble.domain;

import gg.babble.babble.exception.BabbleIllegalArgumentException;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public void join(Room room) {
        if (Objects.nonNull(this.room)) {
            this.room.leave(this);
        }

        this.room = room;

        if (room.hasNotUser(this)) {
            this.room.join(this);
        }
    }

    public void leave(Room room) {
        if (Objects.isNull(this.room) || !this.room.equals(room)) {
            throw new BabbleIllegalArgumentException("해당 방을 나갈 수 없습니다.");
        }

        this.room = null;

        if (room.hasUser(this)) {
            room.leave(this);
        }
    }

    public boolean hasRoom(Room room) {
        return !hasNotRoom(room);
    }

    public boolean hasNotRoom(Room room) {
        return Objects.isNull(this.room) || !this.room.equals(room);
    }
}
