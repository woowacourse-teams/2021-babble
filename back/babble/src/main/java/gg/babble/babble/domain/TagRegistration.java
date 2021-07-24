package gg.babble.babble.domain;

import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import lombok.NonNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tag_registration")
@Entity
public class TagRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @NonNull
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
        TagRegistration tagRegistration = (TagRegistration) o;
        return id.equals(tagRegistration.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

