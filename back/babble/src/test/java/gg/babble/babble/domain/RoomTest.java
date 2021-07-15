package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RoomTest {

    private Room room;

    @BeforeEach
    void setUp() {
        room = prepareRoom();
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

    @DisplayName("방에 유저가 입장한다.")
    @Test
    void joinRoom() {
        User guest = User.builder()
            .id(2L)
            .name("손님")
            .build();

        room.join(guest);

        assertThat(room.getGuests()).hasSize(1);
        assertThat(room.getGuests()).contains(guest);
        assertThat(guest.getRoom()).isEqualTo(room);
    }

    @DisplayName("유저가 이미 방에 있을 때 다시 유저가 방 입장을 요청하면 예외가 발생한다.")
    @Test
    void alreadyJoin() {
        User guest = User.builder()
            .id(2L)
            .name("손님")
            .build();

        room.join(guest);

        assertThatThrownBy(() -> room.join(guest)).isInstanceOf(BabbleDuplicatedException.class);
    }

    @DisplayName("유저가 방에서 나간다.")
    @Test
    void leave() {
        User guest1 = User.builder()
            .id(2L)
            .name("손님")
            .build();
        User guest2 = User.builder()
            .id(3L)
            .name("손님")
            .build();

        room.join(guest1);
        room.join(guest2);
        room.leave(guest1);

        assertThat(room.getGuests()).hasSize(1);
        assertThat(guest1.getRoom()).isNull();
    }

    @DisplayName("존재하지 않는 유저가 방을 나갈 때 예외 처리한다.")
    @Test
    void leavingUserNotFoundInRoom() {
        User user = User.builder()
            .id(2L)
            .name("외부자")
            .build();

        assertThatThrownBy(() -> room.leave(user))
            .isInstanceOf(BabbleNotFoundException.class);
    }
}
