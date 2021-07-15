package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RoomTest {

    @DisplayName("방에 유저가 입장한다.")
    @Test
    void joinRoom() {
        Room room = prepareRoom();
        User guest = User.builder()
            .id(2L)
            .name("손님")
            .build();

        room.join(guest);

        assertThat(room.getGuests()).hasSize(1);
        assertThat(room.getGuests()).contains(guest);
    }

    private Room prepareRoom() {
        Game game = Game.builder()
            .id(1L)
            .name("게임")
            .build();
        User host = User.builder()
            .id(1L)
            .name("방장")
            .build();
        List<Tag> tags = Arrays.asList(
            Tag.builder().name("실버").build(),
            Tag.builder().name("2시간").build());
        return Room.builder()
            .id(1L)
            .game(game)
            .host(host)
            .tags(tags)
            .build();
    }
}
