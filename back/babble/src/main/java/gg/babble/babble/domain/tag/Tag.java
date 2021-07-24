package gg.babble.babble.domain.tag;

import gg.babble.babble.exception.BabbleLengthException;
import javax.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tag {

    @Id
    private String name;

    @Embedded
    private TagRegistrationsOfTag tagRegistrations;

    @Builder
    private Tag(final String name) {
        validate(name);
        this.name = name;
        this.tagRegistrations = new TagRegistrationsOfTag();
    }

    private static void validate(final String name) {
        if (Objects.isNull(name) || name.length() < 1 || name.length() > 8) {
            throw new BabbleLengthException("이름의 길이는 1자 이상 8자 이하입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
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
