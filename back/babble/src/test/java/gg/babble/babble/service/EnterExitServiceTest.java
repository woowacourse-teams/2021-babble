package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.game.Game;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.dto.response.SessionsResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EnterExitServiceTest extends ApplicationTest {

    @Autowired
    private EnterExitService enterExitService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("세션을 생성(방에 유저 입장)한다.")
    @Test
    void createSession() {
        // given
        Game game = gameRepository.save(new Game("게임 이름", Collections.singletonList("게임이미지")));
        List<Tag> tags = Collections.singletonList(tagRepository.save(new Tag("초보만")));
        MaxHeadCount maxHeadCount = new MaxHeadCount(4);
        Room room = roomRepository.save(new Room(game, tags, maxHeadCount));
        User user = userRepository.save(new User("코 파는 알리스타"));

        SessionRequest request = new SessionRequest(user.getId(), "1A2B3C4D");

        // when
        SessionsResponse response = enterExitService.enter(room.getId(), request);

        // then
        assertThat(response.getHost()).usingRecursiveComparison()
            .isEqualTo(user);
    }

    @DisplayName("세션을 삭제(방에 유저 퇴장)한다.")
    @Test
    void deleteSession() {
        // given
        Session session = 세션을_생성한다();

        // when
        enterExitService.exit(session.getSessionId());

        // then
        assertThat(sessionRepository.findBySessionId(session.getSessionId()))
            .isNotPresent();
    }

    @DisplayName("방에 남은 유저가 없을 경우 방이 자동으로 삭제된다.")
    @Test
    void autoDeleteRoom() {
        String gameName = "너구리게임";
        String tagName1 = "2시간";
        String tagName2 = "실버";
        int maxHeadCount = 4;

        Game game = gameRepository.save(new Game(gameName));
        Tag tag1 = tagRepository.save(new Tag(tagName1));
        Tag tag2 = tagRepository.save(new Tag(tagName2));
        User user = userRepository.save(new User("코 파는 알리스타"));
        Room room = roomRepository.save(new Room(game, Arrays.asList(tag1, tag2), new MaxHeadCount(maxHeadCount)));

        Session session = new Session("11112222", user, room);
        sessionRepository.save(session);
        assertThat(roomRepository.findById(room.getId())).isPresent();

        enterExitService.exit(session.getSessionId());
        assertThat(roomRepository.findById(room.getId())).isEmpty();
    }

    private Session 세션을_생성한다() {
        Game game = gameRepository.save(new Game("게임 이름", Collections.singletonList("게임 이미지")));
        List<Tag> tags = Collections.singletonList(tagRepository.save(new Tag("초보만")));
        MaxHeadCount maxHeadCount = new MaxHeadCount(4);
        Room room = roomRepository.save(new Room(game, tags, maxHeadCount));
        User user = userRepository.save(new User("코 파는 알리스타"));

        return sessionRepository.save(new Session("1A2B3C4D", user, room));
    }
}
