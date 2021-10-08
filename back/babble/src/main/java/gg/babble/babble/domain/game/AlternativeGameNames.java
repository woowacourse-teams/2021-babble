package gg.babble.babble.domain.game;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AlternativeGameNames {

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @NotNull(message = "대안 이름들은 Null 일 수 없습니다.")
    private List<AlternativeGameName> elements = new ArrayList<>();

    public AlternativeGameNames(final List<AlternativeGameName> elements) {
        this.elements = elements;
    }

    public void add(final AlternativeGameName name) {
        if (contains(name.getValue())) {
            throw new BabbleDuplicatedException(String.format("이미 존재하는 이름 입니다.(%s)", name.getValue()));
        }

        elements.add(name);
    }

    public void remove(final AlternativeGameName alternativeGameName) {
        if (!contains(alternativeGameName.getValue())) {
            throw new BabbleNotFoundException(String.format("존재하지 않는 이름 입니다.(%s)", alternativeGameName.getValue()));
        }

        elements.remove(alternativeGameName);
    }

    public boolean contains(final String name) {
        return elements.stream()
            .anyMatch(alternativeName -> alternativeName.getValue().equals(name));
    }

    public List<String> getNames() {
        return elements.stream()
            .map(AlternativeGameName::getValue)
            .collect(Collectors.toList());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlternativeGameNames that = (AlternativeGameNames) o;
        return Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }
}
