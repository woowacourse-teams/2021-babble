package gg.babble.babble.domain.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.game.Game;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleIllegalStatementException;
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
}
