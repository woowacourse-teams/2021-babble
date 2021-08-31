package gg.babble.babble.domain.room;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Rooms {

    @OneToMany(mappedBy = "game")
    private final List<Room> rooms;

    public Rooms() {
        this(new ArrayList<>());
    }

    public Rooms(final List<Room> rooms) {
        this.rooms = rooms;
    }

    public int totalHeadCount() {
        return rooms.stream()
            .mapToInt(Room::currentHeadCount)
            .sum();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }
}
