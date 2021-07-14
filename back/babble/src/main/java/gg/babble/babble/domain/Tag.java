package gg.babble.babble.domain;

import gg.babble.babble.exception.BabbleLengthException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@Entity
public class Tag {

    @Id
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Room> rooms;

    private Tag(String name, List<Room> rooms) {
        validate(name);
        this.name = name;
        this.rooms = new ArrayList<>(rooms);
    }

    private static void validate(String name) {
        if (Objects.isNull(name) || name.length() < 1 || name.length() > 8) {
            throw new BabbleLengthException("이름의 길이는 1자 이상 8자 이하입니다.");
        }
    }
}
