package gg.babble.babble.domain.tag;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import gg.babble.babble.exception.BabbleLengthException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TagName name;

    @Embedded
    private AlternativeTagNames alternativeTagNames;

    @Embedded
    private TagRegistrationsOfTag tagRegistrations;

    public Tag(final String name) {
        this(null, name);
    }

    public Tag(final Long id, final String name) {
        this(id, name, new AlternativeTagNames(), new TagRegistrationsOfTag());
    }

    public Tag(final Long id, final String name, final AlternativeTagNames alternativeTagNames, final TagRegistrationsOfTag tagRegistrations) {
        this.id = id;
        this.name = new TagName(name);
        this.alternativeTagNames = alternativeTagNames;
        this.tagRegistrations = tagRegistrations;
    }

    public void addAlternativeName(final AlternativeTagName name) {
        if (hasName(name.getName())) {
            throw new BabbleDuplicatedException(String.format("이미 존재하는 이름 입니다.(%s)", name.getName()));
        }

        alternativeTagNames.add(name);

        if (name.getTag() != this) {
            name.setTag(this);
        }
    }

    public boolean hasName(final TagName name) {
        return this.name.equals(name) || alternativeTagNames.contains(name);
    }

    public boolean hasNotName(final TagName name) {
        return !hasName(name);
    }

    public String getName() {
        return name.getValue();
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
