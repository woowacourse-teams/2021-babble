package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.game.Game;
import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class SessionRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @DisplayName("세션 생성을 수행한다.")
    @Test
    void saveSession() {
        // given
        Session session = 세션_객체를_생성한다();

        // when
        Session savedSession = sessionRepository.save(session);

        // then
        assertThat(savedSession.getId()).isNotNull();
        assertThat(savedSession.getSessionId()).isEqualTo(session.getSessionId());
        assertThat(savedSession.getRoom()).isEqualTo(session.getRoom());
        assertThat(savedSession.getUser()).isEqualTo(session.getUser());
    }

    @DisplayName("세션 삭제를 수행한다.")
    @Test
    void deleteSession() {
        // given
        Session session = sessionRepository.save(세션_객체를_생성한다());

        // when
        session.delete();

        // then
        assertThat(sessionRepository.findBySessionIdAndDeletedFalse(session.getSessionId())).isNotPresent();
    }

    @DisplayName("sessionId로 조회를 시도할 때 삭제된 세션은 조회되지 않는다.")
    @Test
    void findByDeletedFalse() {
        // given
        Session session = sessionRepository.save(세션_객체를_생성한다());

        // then
        assertThat(sessionRepository.findBySessionIdAndDeletedFalse(session.getSessionId())).isPresent();
        session.delete();
        assertThat(sessionRepository.findBySessionIdAndDeletedFalse(session.getSessionId())).isNotPresent();
    }

    private Session 세션_객체를_생성한다() {
        Tag tag = tagRepository.save(new Tag("초보만"));
        List<Tag> tags = Collections.singletonList(tag);
        Game game = gameRepository.save(new Game("게임 이름", "게임 이미지"));
        MaxHeadCount maxHeadCount = new MaxHeadCount(4);
        Room room = roomRepository.save(new Room(game, tags, maxHeadCount));
        User user = userRepository.save(new User("코 파는 알리스타"));

        return new Session("1A2B3C4D", user, room);
    }
}