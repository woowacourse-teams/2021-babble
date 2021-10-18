package gg.babble.babble.domain;

import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@AllArgsConstructor
@SQLDelete(sql = "UPDATE tag_registration SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tag_registration")
@Entity
public class TagRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "방은 Null 일 수 없습니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @NotNull(message = "태그는 Null 일 수 없습니다.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    private final boolean deleted = false;

    public TagRegistration(final Room room, final Tag tag) {
        this(null, room, tag);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TagRegistration that = (TagRegistration) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

