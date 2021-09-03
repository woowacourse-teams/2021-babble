package gg.babble.babble.domain.game;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AlternativeGameNames {

    @OneToMany(mappedBy = "game")
    @NotNull(message = "대안 이름들은 Null 일 수 없습니다.")
    private Set<AlternativeGameName> alternativeGameNames = new HashSet<>();

    public AlternativeGameNames(final Set<AlternativeGameName> alternativeGameNames) {
        this.alternativeGameNames = alternativeGameNames;
    }

    public void add(final AlternativeGameName name) {
        alternativeGameNames.add(name);
    }

    public boolean contains(final String name) {
        return alternativeGameNames.stream()
            .anyMatch(alternativeName -> alternativeName.getName().equals(name));
    }

    public Set<String> getNames() {
        return alternativeGameNames.stream()
            .map(AlternativeGameName::getName)
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
        final AlternativeGameNames that = (AlternativeGameNames) o;
        return Objects.equals(alternativeGameNames, that.alternativeGameNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alternativeGameNames);
    }
}
