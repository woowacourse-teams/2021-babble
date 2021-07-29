package gg.babble.babble.domain.tag;

import gg.babble.babble.exception.BabbleLengthException;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "태그 이름은 Null 일 수 없습니다.")
    private String name;

    @Embedded
    private TagRegistrationsOfTag tagRegistrations;

    public Tag(final String name) {
        this(null, name);
    }

    public Tag(final Long id, final String name) {
        validateToConstruct(name);
        this.id = id;
        this.name = name;
        this.tagRegistrations = new TagRegistrationsOfTag();
    }

    private static void validateToConstruct(final String name) {
        if (Objects.isNull(name) || name.length() < 1 || name.length() > 8) {
            throw new BabbleLengthException("이름의 길이는 1자 이상 8자 이하입니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
