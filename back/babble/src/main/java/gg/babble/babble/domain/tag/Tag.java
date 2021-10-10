package gg.babble.babble.domain.tag;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleIllegalStatementException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@SQLDelete(sql = "UPDATE tag SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
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

    @Column(nullable = false)
    private boolean deleted = false;

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

    public void addNames(final List<String> names) {
        List<TagName> tagNames = names.stream()
            .map(TagName::new)
            .collect(Collectors.toList());
        validateToAddNames(tagNames);

        for (TagName tagName : tagNames) {
            new AlternativeTagName(tagName, this);
        }
    }

    private void validateToAddNames(final List<TagName> names) {
        Set<TagName> nameSet = new HashSet<>(names);
        if (nameSet.size() != names.size()) {
            throw new BabbleDuplicatedException(String.format("중복된 값이 있습니다.(%s)", Strings.join(names, ',')));
        }

        for (TagName name : names) {
            if (hasName(name)) {
                throw new BabbleDuplicatedException(String.format("이미 존재하는 이름 입니다.(%s)", name.getValue()));
            }
        }
    }

    public void addAlternativeName(final AlternativeTagName name) {
        if (hasName(name.getValue())) {
            throw new BabbleDuplicatedException(String.format("이미 존재하는 이름 입니다.(%s)", name.getValue()));
        }

        alternativeTagNames.add(name);

        if (name.getTag() != this) {
            name.setTag(this);
        }
    }

    public void update(String name, List<String> alternativeNames) {
        this.name = new TagName(name);
        this.alternativeTagNames.deleteAll();
        AlternativeTagNames.convertAndAddToTag(alternativeNames, this);
    }

    public void removeAlternativeName(final AlternativeTagName alternativeTagName) {
        if (hasNotName(alternativeTagName.getValue())) {
            throw new BabbleNotFoundException(String.format("존재하지 않는 이름 입니다.(%s)", alternativeTagName.getValue()));
        }

        alternativeTagNames.remove(alternativeTagName);
    }

    public void delete() {
        if (tagRegistrations.isNotEmpty()) {
            throw new BabbleIllegalStatementException(String.format("(%d) 태그를 사용중인 방이 있어 삭제를 할 수 없습니다.", id));
        }

        alternativeTagNames.deleteAll();
        deleted = true;
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
        final Tag tag = (Tag) o;
        return Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
