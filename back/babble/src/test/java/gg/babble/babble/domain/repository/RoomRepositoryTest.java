package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.game.Game;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleIllegalStatementException;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @DisplayName("방을 생성한다.")
    @Test
    void createRoom() {
        // given
        Game game = gameRepository.save(new Game("게임 이름", "게임 이미지"));
        List<Tag> tags = Collections.singletonList(tagRepository.save(new Tag("초보만")));
        MaxHeadCount maxHeadCount = new MaxHeadCount(4);
        Room room = new Room(game, tags, maxHeadCount);

        // when
        Room savedRoom = roomRepository.save(room);

        // then
        assertThat(savedRoom.getId()).isNotNull();
        assertThat(savedRoom.getGame()).isEqualTo(game);
        assertThat(savedRoom.getTagRegistrationsOfRoom().tags()).isEqualTo(tags);
        assertThat(savedRoom.getMaxHeadCount()).isEqualTo(maxHeadCount);
        assertThatThrownBy(savedRoom::getHost).isInstanceOf(BabbleIllegalStatementException.class);
    }

    @DisplayName("ID를 통해 방을 조회한다.")
    @Test
    void findRoomById() {
        // given
        Game game = gameRepository.save(new Game("게임 이름", "게임 이미지"));
        List<Tag> tags = Collections.singletonList(tagRepository.save(new Tag("초보만")));
        MaxHeadCount maxHeadCount = new MaxHeadCount(4);

        Room room = roomRepository.save(new Room(game, tags, maxHeadCount));

        // when
        Room savedRoom = roomRepository.findByIdAndDeletedFalse(room.getId())
            .orElseThrow(BabbleNotFoundException::new);

        // then
        assertThat(savedRoom.getId()).isNotNull();
        assertThat(savedRoom.getGame()).isEqualTo(game);
        assertThat(savedRoom.getTagRegistrationsOfRoom().tags()).isEqualTo(tags);
        assertThat(savedRoom.getMaxHeadCount()).isEqualTo(maxHeadCount);
        assertThatThrownBy(savedRoom::getHost).isInstanceOf(BabbleIllegalStatementException.class);
    }

    @DisplayName("삭제된 방은 조회되지 않는다.")
    @Test
    void findRoomByIdException() {
        // given
        Game game = gameRepository.save(new Game("게임 이름", "게임 이미지"));
        List<Tag> tags = Collections.singletonList(tagRepository.save(new Tag("초보만")));
        MaxHeadCount maxHeadCount = new MaxHeadCount(4);
        User user = userRepository.save(new User("와일더"));
        Room room = roomRepository.save(new Room(game, tags, maxHeadCount));

        Session session = new Session("1234", user, room);
        sessionRepository.save(session);

        // when
        room.exitSession(session);

        // then
        assertThat(room.isDeleted()).isTrue();
        assertThat(roomRepository.findByIdAndDeletedFalse(room.getId())).isNotPresent();
    }

    @DisplayName("게임 ID를 기준으로 최신순 방 목록을 검색한다.")
    @Test
    void findByGameId() {
        // given
        Game game = gameRepository.save(new Game("게임 이름", "게임 이미지"));
        Tag tag = tagRepository.save(new Tag("초보만"));
        List<Tag> tags = new ArrayList<>(Collections.singletonList(tag));
        Room room1 = createRoomWithGameAndTags(game, tags);
        Room room2 = createRoomWithGameAndTags(game, tags);

        // when
        Pageable pageable = PageRequest.of(0, 16);
        List<Room> rooms = roomRepository.findByGameIdAndDeletedFalse(game.getId(), pageable);

        // then
        assertThat(rooms).containsExactly(room2, room1);
    }

    @DisplayName("게임 ID와 태그를 기준으로 최신순 방 목록을 검색한다.")
    @Test
    void findByGameIdAndTags() {
        // given
        Game game = gameRepository.save(new Game("게임 이름", "게임 이미지"));
        Tag tag1 = tagRepository.save(new Tag("2시간"));
        Tag tag2 = tagRepository.save(new Tag("실버"));
        Tag tag3 = tagRepository.save(new Tag("고수만"));
        Tag tag4 = tagRepository.save(new Tag("초보만"));

        Room room1 = createRoomWithGameAndTags(game, Arrays.asList(tag1, tag2));
        Room room2 = createRoomWithGameAndTags(game, Arrays.asList(tag1, tag2));
        Room room3 = createRoomWithGameAndTags(game, Arrays.asList(tag1, tag2, tag3));
        Room room4 = createRoomWithGameAndTags(game, Collections.singletonList(tag4));

        // when
        Pageable pageable = PageRequest.of(0, 16);
        Set<Long> tagIds = new HashSet<>(Arrays.asList(tag1.getId(), tag2.getId()));
        List<Room> rooms = roomRepository.findByGameIdAndTagIdsAndDeletedFalse(game.getId(), tagIds, (long) tagIds.size(), pageable);

        // then
        assertThat(rooms).containsExactly(room3, room2, room1);
    }

    private Room createRoomWithGameAndTags(final Game game, final List<Tag> tags) {
        MaxHeadCount maxHeadCount = new MaxHeadCount(4);
        User user = userRepository.save(new User("user"));

        Room room = roomRepository.save(new Room(game, tags, maxHeadCount));
        sessionRepository.save(new Session(String.valueOf(user.getId()), user, room));
        return room;
    }
}
