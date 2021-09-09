package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.game.Game;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.exception.BabbleIllegalStatementException;
import gg.babble.babble.service.auth.SubscribeAuthService;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscribeAuthServiceTest extends ApplicationTest {

    @Autowired
    private SubscribeAuthService subscribeAuthService;

    @Autowired
    private EnterExitService enterExitService;

    @DisplayName("새로운 유저가 방에 들어오더라도, 정원초과가 발생하지 않으면 예외가 발생하지 않는다.")
    @Test
    void userJoinTest() {
        // given
        Room room = prepareDummyRoom();

        // when, then
        assertThatCode(() -> {
            subscribeAuthService.validate(String.format("/topic/rooms/%d/users", room.getId()));
        }).doesNotThrowAnyException();
    }


    @DisplayName("방 인원수보다 더 많은 인원이 입장하려고 하는경우, 예외가 발생한다.")
    @Test
    void overcrowdExceptionTest() {
        // given
        Room room = prepareDummyRoom();
        User 포츈 = userRepository.save(new User("포츈"));
        enterExitService.enter(room.getId(), new SessionRequest(포츈.getId(), "1111"));

        // when, then
        assertThatThrownBy(
            () -> subscribeAuthService.validate(String.format("/topic/rooms/%d/users", room.getId()))
        ).isInstanceOf(BabbleIllegalStatementException.class);
    }

    private Room prepareDummyRoom() {
        Game game = gameRepository.save(new Game("APEX_LEGEND"));
        Tag tag = tagRepository.save(new Tag("2시간"));
        Room room = roomRepository.save(new Room(game, Collections.singletonList(tag), new MaxHeadCount(2)));
        User 루트 = userRepository.save(new User("루트"));

        enterExitService.enter(room.getId(), new SessionRequest(루트.getId(), "1111"));

        return room;
    }
}
