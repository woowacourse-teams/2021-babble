package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.repository.SessionRepository;
import gg.babble.babble.domain.repository.TagRepository;
import gg.babble.babble.domain.repository.UserRepository;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.SessionRequest;
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

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TagRepository tagRepository;

    @DisplayName("세션을 생성(방에 유저 입장)한다.")
    @Test
    void createSession() {
        // given
        Game game = gameRepository.save(new Game("게임 이름", "게임 이미지"));
        List<Tag> tags = Collections.singletonList(tagRepository.save(new Tag("초보만")));
        MaxHeadCount maxHeadCount = new MaxHeadCount(4);
        Room room = roomRepository.save(new Room(game, tags, maxHeadCount));
        User user = userRepository.save(new User("코 파는 알리스타"));

        SessionRequest request = new SessionRequest(user.getId(), "1A2B3C4D");

        // when
        sessionService.create(room.getId(), request);

        // then
        Session createdSession = sessionRepository.findBySessionIdAndDeletedFalse(request.getSessionId())
            .orElseThrow(BabbleNotFoundException::new);

        assertThat(createdSession.getId()).isNotNull();
        assertThat(createdSession.getSessionId()).isEqualTo(request.getSessionId());
        assertThat(createdSession.getRoom()).isEqualTo(room);
        assertThat(createdSession.getUser()).isEqualTo(user);
    }

    @DisplayName("세션과 매핑된 방 ID를 조회한다.")
    @Test
    void findRoomBySessionId() {
        // given
        Session session = 세션을_생성한다();

        // when
        Long roomId = sessionService.findRoomIdBySessionId(session.getSessionId());

        // then
        assertThat(roomId).isEqualTo(session.getRoom().getId());
    }

    @DisplayName("세션과 매핑된 방 ID 조회 실패시 예외가 발생한다.")
    @Test
    void findSessionOrElseThrowException() {
        // then
        assertThatThrownBy(() -> sessionService.findRoomIdBySessionId("아무거나아아"))
            .isInstanceOf(BabbleNotFoundException.class);
    }

    @DisplayName("세션을 삭제(방에 유저 퇴장)한다.")
    @Test
    void deleteSession() {
        // given
        Session session = 세션을_생성한다();
        session.userEnterRoom();

        // when
        sessionService.delete(session.getSessionId());

        // then
        assertThat(sessionRepository.findBySessionIdAndDeletedFalse(session.getSessionId()))
            .isNotPresent();
    }

    Session 세션을_생성한다() {
        Game game = gameRepository.save(new Game("게임 이름", "게임 이미지"));
        List<Tag> tags = Collections.singletonList(tagRepository.save(new Tag("초보만")));
        MaxHeadCount maxHeadCount = new MaxHeadCount(4);
        Room room = roomRepository.save(new Room(game, tags, maxHeadCount));
        User user = userRepository.save(new User("코 파는 알리스타"));
        String sessionId = "1A2B3C4D";

        return sessionRepository.save(new Session(sessionId, room, user));
    }
}