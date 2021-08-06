package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleIllegalStatementException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RoomTest {

    private Room room;
    private Game game;
    private List<Tag> tags;

    @BeforeEach
    void setUp() {
        User host = new User(1L, "방장");
        game = new Game(1L, "게임");
        tags = Arrays.asList(new Tag("실버"), new Tag("2시간"));
        room = generateEmptyRoom();

        room.join(host);
    }

    private Room generateEmptyRoom() {
        return new Room(1L, game, tags, new MaxHeadCount(4));
    }

    @DisplayName("방에 유저가 입장한다.")
    @Test
    void joinRoom() {
        User guest = new User(2L, "손님");

        room.join(guest);

        assertThat(room.getGuests()).hasSize(1);
        assertThat(room.getGuests()).contains(guest);
        assertThat(guest.getRoom()).isEqualTo(room);
    }

    @DisplayName("유저가 이미 방에 있을 때 다시 유저가 방 입장을 요청하면 예외가 발생한다.")
    @Test
    void alreadyJoin() {
        User guest = new User(2L, "손님");

        room.join(guest);

        assertThatThrownBy(() -> room.join(guest)).isInstanceOf(BabbleDuplicatedException.class);
    }

    @DisplayName("유저가 방에서 나간다.")
    @Test
    void leave() {
        User guest1 = new User(2L, "손님");
        User guest2 = new User(3L, "손님");

        room.join(guest1);
        room.join(guest2);
        room.leave(guest1);

        assertThat(room.getGuests()).hasSize(1);
        assertThat(guest1.getRoom()).isNull();
    }

    @DisplayName("존재하지 않는 유저가 방을 나갈 때 예외 처리한다.")
    @Test
    void leavingUserNotFoundInRoom() {
        User user = new User(2L, "외부자");

        assertThatThrownBy(() -> room.leave(user))
            .isInstanceOf(BabbleNotFoundException.class);
    }

    @DisplayName("호스트가 퇴장할 경우 가장 먼저 들어온 게스트가 호스트가 된다.")
    @Test
    void hostDelegate() {
        User guest1 = new User(2L, "게스트");
        User guest2 = new User(3L, "게스트");
        User host = room.getHost();

        room.join(guest1);
        room.join(guest2);
        room.leave(host);

        assertThat(room.getGuests()).hasSize(1);
        assertThat(room.getHost()).isEqualTo(guest1);
        assertThat(host.getRoom()).isNull();
        assertThat(guest1.getRoom()).isEqualTo(room);
    }

    @DisplayName("호스트를 조회한다.")
    @Test
    void getHost() {
        assertThat(room.getHost()).isEqualTo(
            new User(1L, "방장")
        );
    }

    @DisplayName("빈 방의 호스트를 조회하면 예외 처리한다.")
    @Test
    void getInvalidHost() {
        Room emptyRoom = generateEmptyRoom();

        assertThatThrownBy(emptyRoom::getHost).isInstanceOf(BabbleIllegalStatementException.class);
    }

    @DisplayName("게스트를 조회한다.")
    @Test
    void getGuests() {
        assertThat(room.getGuests()).isEmpty();
    }

    @DisplayName("빈 방의 게스트를 조회하면 예외 처리한다.")
    @Test
    void getInvalidGuests() {
        Room emptyRoom = generateEmptyRoom();

        assertThatThrownBy(emptyRoom::getGuests)
            .isInstanceOf(BabbleIllegalStatementException.class);
    }
}
