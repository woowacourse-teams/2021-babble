package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.repository.SessionRepository;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SessionServiceTest extends ApplicationTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @DisplayName("세션을 생성한다.")
    @Test
    void createSession() {
        // given
        Game game = new Game(1L, "게임 이름", "게임 이미지");
        List<Tag> tags = Collections.singletonList(new Tag(1L, "초보만"));
        MaxHeadCount maxHeadCount = new MaxHeadCount(4);
        Room room = new Room(1L, game, tags, maxHeadCount);
        User user = new User(1L, "코 파는 알리스타");
        String sessionId = "1A2B3C4D";

        // when
        sessionService.userEnterRoom(sessionId, room, user);

        // then
        Session createdSession = sessionRepository.findBySessionIdAndDeletedFalse(sessionId)
            .orElseThrow(BabbleNotFoundException::new);

        assertThat(createdSession.getId()).isNotNull();
        assertThat(createdSession.getSessionId()).isEqualTo(sessionId);
        assertThat(createdSession.getRoom()).isEqualTo(room);
        assertThat(createdSession.getUser()).isEqualTo(user);
    }

    @DisplayName("세션과 매핑된 방을 조회한다.")
    @Test
    void findRoomBySessionId() {
        // given
        Session session = 세션을_생성한다();

        // when
        Room room = sessionService.findRoomBySessionId(session.getSessionId());

        // then
        assertThat(room).isEqualTo(session.getRoom());
    }

    @DisplayName("세션과 매핑된 유저를 조회한다.")
    @Test
    void findUserBySessionId() {
        // given
        Session session = 세션을_생성한다();

        // when
        User user = sessionService.findUserBySessionId(session.getSessionId());

        // then
        assertThat(user).isEqualTo(session.getUser());
    }

    @DisplayName("세션 매핑 정보를 조회할 때 찾지 못할 경우 예외가 발생한다.")
    @Test
    void findSessionOrElseThrowException() {
        // then
        assertThatThrownBy(() -> sessionService.findRoomBySessionId("아무거나아아"))
            .isInstanceOf(BabbleNotFoundException.class);
        assertThatThrownBy(() -> sessionService.findUserBySessionId("아무거나아아"))
            .isInstanceOf(BabbleNotFoundException.class);
    }

    @DisplayName("세션을 삭제한다.")
    @Test
    void deleteSession() {
        // given
        Session session = 세션을_생성한다();

        // when
        sessionService.deleteSessionBySessionId(session.getSessionId());

        // then
        assertThat(sessionRepository.findBySessionIdAndDeletedFalse(session.getSessionId()))
            .isNotPresent();
    }

    Session 세션을_생성한다() {
        Game game = new Game(1L, "게임 이름", "게임 이미지");
        List<Tag> tags = Collections.singletonList(new Tag(1L, "초보만"));
        MaxHeadCount maxHeadCount = new MaxHeadCount(4);
        Room room = new Room(1L, game, tags, maxHeadCount);
        User user = new User(1L, "코 파는 알리스타");
        String sessionId = "1A2B3C4D";

        return sessionRepository.save(new Session(sessionId, room, user));
    }
}