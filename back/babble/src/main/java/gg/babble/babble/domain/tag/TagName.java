package gg.babble.babble.domain.tag;

import gg.babble.babble.exception.BabbleIllegalArgumentException;
import gg.babble.babble.exception.BabbleLengthException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class TagName {

    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 20;

    @Column(name = "name")
    @NotNull(message = "태그 이름은 Null 일 수 없습니다.")
    private String value;

    public TagName(final String name) {
        validateToConstruct(name);
        this.value = name;
    }

    private static void validateToConstruct(final String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new BabbleLengthException(
                String.format("이름의 길이는 %d자 이상 %d자 이하입니다. 현재 이름 길이(%d)", MIN_NAME_LENGTH, MAX_NAME_LENGTH, name.length())
            );
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
        final TagName tagName = (TagName) o;
        return Objects.equals(value, tagName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
