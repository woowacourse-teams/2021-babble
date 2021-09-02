package gg.babble.babble.domain.game;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AlternativeNames {

    @OneToMany(mappedBy = "game")
    @NotNull(message = "대안 이름들은 Null 일 수 없습니다.")
    private Set<AlternativeName> alternativeNames = new HashSet<>();

    public AlternativeNames(final Set<AlternativeName> alternativeNames) {
        this.alternativeNames = alternativeNames;
    }

    public void add(final AlternativeName name) {
        alternativeNames.add(name);
    }

    public boolean contains(final String name) {
        return alternativeNames.stream()
            .anyMatch(alternativeName -> alternativeName.getName().equals(name));
    }

    public Set<String> getNames() {
        return alternativeNames.stream()
            .map(AlternativeName::getName)
            .collect(Collectors.toSet());
    }
}
