package gg.babble.babble.domain;

import gg.babble.babble.exception.BabbleLengthException;
import java.util.ArrayList;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class Tag {

    @Id
    private String name;

    @OneToMany(mappedBy = "tag")
    private List<TagRegistration> tagRegistrations;

    @Builder
    private Tag(final String name) {
        validate(name);
        this.name = name;
        this.tagRegistrations = new ArrayList<>();
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
        return name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
