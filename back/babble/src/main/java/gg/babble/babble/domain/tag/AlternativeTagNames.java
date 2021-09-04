package gg.babble.babble.domain.tag;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Embeddable
public class AlternativeTagNames {

    @OneToMany(mappedBy = "tag")
    @NotNull(message = "대안 이름들은 Null 일 수 없습니다.")
    private final Set<AlternativeTagName> elements;

    public AlternativeTagNames() {
        this(new HashSet<>());
    }

    public AlternativeTagNames(final Set<AlternativeTagName> elements) {
        this.elements = elements;
    }

    public void add(final AlternativeTagName name) {
        if (contains(name.getValue())) {
            throw new BabbleDuplicatedException(String.format("이미 존재하는 이름 입니다.(%s)", name.getValue()));
        }
        elements.add(name);
    }

    public void remove(final AlternativeTagName alternativeTagName) {
        if (!contains(alternativeTagName.getValue())) {
            throw new BabbleNotFoundException(String.format("존재하지 않는 이름 입니다.(%s)", alternativeTagName.getValue().getValue()));
        }

        elements.remove(alternativeTagName);
    }

    public boolean contains(final TagName name) {
        return elements.stream()
            .anyMatch(alternativeName -> alternativeName.isSameName(name));
    }

    public Set<String> getNames() {
        return elements.stream()
            .map(AlternativeTagName::getValue)
            .map(TagName::getValue)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlternativeTagNames that = (AlternativeTagNames) o;
        return Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }
}
