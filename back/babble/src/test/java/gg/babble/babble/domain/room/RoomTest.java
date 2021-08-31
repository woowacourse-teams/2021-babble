package gg.babble.babble.domain.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.Session;
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
    private User host;
    private Session session;

    @BeforeEach
    void setUp() {
        host = new User(1L, "방장");
        game = new Game(1L, "게임");
        tags = Arrays.asList(new Tag("실버"), new Tag("2시간"));
        room = new Room(1L, game, tags, new MaxHeadCount(4));
        session = new Session(1L, "1111", host, room);
    }

    @DisplayName("방장만 있는 방에 유저가 입장한다.")
    @Test
    void joinRoom() {
        // given
        User guest = new User(2L, "손님");
        Session session = new Session(2L, "2222", guest, room);

        // then
        assertThat(room.getHost()).isEqualTo(host);
        assertThat(room.getGuests()).containsExactly(guest);
        assertThat(room.currentHeadCount()).isEqualTo(2);
    }

    @DisplayName("유저가 이미 방에 있을 때 다시 유저가 방 입장을 요청하면 예외가 발생한다.")
    @Test
    void alreadyJoin() {
        // given
        User guest = new User(2L, "손님");
        Session session = new Session(2L, "2222", guest, room);

        // when, then
        assertThatThrownBy(() -> room.enterSession(session)).isInstanceOf(BabbleDuplicatedException.class);
    }

    @DisplayName("유저가 방에서 나간다.")
    @Test
    void leave() {
        // given
        User guest1 = new User(2L, "손님");
        User guest2 = new User(3L, "손님");
        Session session1 = new Session(2L, "1234", guest1, room);
        Session session2 = new Session(3L, "2345", guest2, room);

        // when
        room.exitSession(session1);

        // then
        assertThat(guest1.getSession()).isNull();
        assertThat(room.getGuests()).containsExactly(guest2);
        assertThat(room.currentHeadCount()).isEqualTo(2);
    }

    @DisplayName("입장하지 않는 유저가 방을 나갈 때 예외 처리한다.")
    @Test
    void leavingUserNotFoundInRoom() {
        // given
        Room anotherRoom = new Room(game, tags, new MaxHeadCount(4));
        User user = new User(2L, "외부자");
        Session session = new Session(2L, "1234", user, anotherRoom);

        // when, then
        assertThatThrownBy(() -> room.exitSession(session)).isInstanceOf(BabbleNotFoundException.class);
    }

    @DisplayName("호스트가 퇴장할 경우 가장 먼저 들어온 게스트가 호스트가 된다.")
    @Test
    void hostDelegate() {
        // given
        User guest1 = new User(2L, "게스트");
        User guest2 = new User(3L, "게스트");

        Session session1 = new Session(2L, "2222", guest1, room);
        Session session2 = new Session(3L, "3333", guest2, room);

        // when
        room.exitSession(session);

        // then
        assertThat(room.getHost()).isEqualTo(guest1);
        assertThat(room.getGuests()).containsExactly(guest2);
    }

    @DisplayName("호스트를 조회한다.")
    @Test
    void getHost() {
        // when, then
        assertThat(room.getHost()).usingRecursiveComparison()
            .isEqualTo(host);
    }

    @DisplayName("빈 방의 호스트를 조회하면 예외 처리한다.")
    @Test
    void getInvalidHost() {
        // given
        game = new Game(1L, "게임");
        tags = Arrays.asList(new Tag("실버"), new Tag("2시간"));
        room = new Room(1L, game, tags, new MaxHeadCount(4));

        // when, then
        assertThatThrownBy(room::getHost).isInstanceOf(BabbleIllegalStatementException.class);
    }

    @DisplayName("게스트를 조회한다.")
    @Test
    void getGuests() {
        // when, then
        assertThat(room.getGuests()).isEmpty();

        // given
        User guest = new User(2L, "손님");
        Session session = new Session(2L, "2222", guest, room);

        // when, then
        assertThat(room.getGuests()).containsExactly(guest);
    }

    @DisplayName("빈 방의 게스트를 조회하면 예외 처리한다.")
    @Test
    void getInvalidGuests() {
        game = new Game(1L, "게임");
        tags = Arrays.asList(new Tag("실버"), new Tag("2시간"));
        room = new Room(1L, game, tags, new MaxHeadCount(4));

        assertThatThrownBy(room::getGuests)
            .isInstanceOf(BabbleIllegalStatementException.class);
    }

    @DisplayName("방에 모든 인원이 퇴장할 경우 방은 제거된다.")
    @Test
    void deleteRoom() {
        // when, then
        assertThat(room.isDeleted()).isFalse();

        // when
        room.exitSession(session);

        // then
        assertThat(room.isDeleted()).isTrue();
    }
}
