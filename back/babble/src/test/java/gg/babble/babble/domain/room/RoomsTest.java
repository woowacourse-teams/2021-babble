package gg.babble.babble.domain.room;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoomsTest {

    @Test
    @DisplayName("현재 방들에 참여중인 총 유저의 수를 반환한다.")
    void totalUserCountTest() {
        // given
        List<Room> roomList = 방_목록을_생성한다();
        Rooms rooms = new Rooms(roomList);

        int expectedTotalUserCount = roomList.stream()
            .mapToInt(Room::currentHeadCount)
            .sum();

        // when
        int totalUserCount = rooms.totalHeadCount();

        // then
        assertThat(totalUserCount).isEqualTo(expectedTotalUserCount);
    }

    private List<Room> 방_목록을_생성한다() {
        List<Room> roomList = new ArrayList<>();
        Game game = new Game(1L, "fun");

        for (int i = 0; i < 5; i++) {
            Room room = 게임에_해당하는_방을_생성한다(game);
            User user = new User("익명의 누군가");
            Session session = new Session((long) i, Integer.toString(i), room, user);
            room.addSession(session);
            roomList.add(room);
        }

        return roomList;
    }

    private Room 게임에_해당하는_방을_생성한다(final Game game) {
        List<Tag> tags = Collections.singletonList(new Tag("good-tag"));
        MaxHeadCount maxHeadCount = new MaxHeadCount(10);
        return new Room(1L, game, tags, maxHeadCount);
    }
}