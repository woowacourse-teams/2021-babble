package gg.babble.babble.domain.tag;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import gg.babble.babble.exception.BabbleLengthException;
import java.util.Objects;
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

    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "태그 이름은 Null 일 수 없습니다.")
    private String name;

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
        validateToConstruct(name);
        this.id = id;
        this.name = name;
        this.alternativeTagNames = alternativeTagNames;
        this.tagRegistrations = tagRegistrations;
    }

    private static void validateToConstruct(final String name) {
        if (Objects.isNull(name)) {
            throw new BabbleIllegalArgumentException("태그 이름은 Null 일 수 없습니다.");
        }
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new BabbleLengthException(
                String.format("이름의 길이는 %d자 이상 %d자 이하입니다. 현재 이름 길이(%d)", MIN_NAME_LENGTH, MAX_NAME_LENGTH, name.length())
            );
        }
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

    public boolean hasName(final String name) {
        return this.name.equals(name) || alternativeTagNames.contains(name);
    }

    public boolean hasNotName(final String name) {
        return !hasName(name);
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
