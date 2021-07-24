package gg.babble.babble.domain;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_tag")
@Entity
public class RoomTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "tag_name")
    private Tag tag;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomTag roomTag = (RoomTag) o;
        return id.equals(roomTag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
