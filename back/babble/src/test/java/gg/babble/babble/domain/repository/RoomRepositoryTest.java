package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.Room;
import gg.babble.babble.domain.Tag;
import gg.babble.babble.domain.User;
import gg.babble.babble.exception.BabbleDuplicatedException;
import gg.babble.babble.exception.BabbleNotFoundException;
import gg.babble.babble.service.GameService;
import gg.babble.babble.service.TagService;
import gg.babble.babble.service.UserService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class RoomRepositoryTest extends ApplicationTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @DisplayName("방 더미 데이터를 확인한다.")
    @Test
    void dummyGameTest() {
        Optional<Room> room = roomRepository.findById(1L);

        Game expectedGame = Game.builder()
            .id(1L)
            .name("League Of Legend")
            .build();
        User expectedHost = User.builder()
            .id(1L)
            .name("루트")
            .build();

        List<Tag> expectedTags = Arrays.asList(Tag.builder()
                .name("실버")
                .build(),
            Tag.builder()
                .name("2시간")
                .build()
        );

        assertThat(room.isPresent()).isTrue();
        assertThat(room.get().getCreatedDate()).isNotNull();
        assertThat(room.get().getGame()).usingRecursiveComparison()
            .isEqualTo(expectedGame);
        assertThat(room.get().getHost()).usingRecursiveComparison()
            .isEqualTo(expectedHost);
        assertThat(room.get().getTags()).usingRecursiveComparison()
            .ignoringFields("rooms")
            .isEqualTo(expectedTags);
    }

    @DisplayName("생성한 방을 저장한다.")
    @Test
    void saveTest() {
        Room room = saveRoom();
        roomRepository.flush();

        assertThat(roomRepository.existsById(room.getId())).isTrue();
    }

    private Room saveRoom() {
        Game game = gameService.findById(1L);
        User user = userService.findById(1L);
        List<Tag> tags = Arrays.asList(tagService.findById("실버"),
            tagService.findById("2시간"));

        return roomRepository.save(Room.builder()
            .game(game)
            .host(user)
            .tags(tags).build());
    }

    @DisplayName("방 생성 시각을 저장한다.")
    @Test
    void saveTimeOfRoomTest() {
        Room room = saveRoom();

        assertThat(room.getCreatedDate()).isNotNull();
    }

    @DisplayName("방에 유저가 입장한다.")
    @Test
    void joinRoom() {
        Room room = roomRepository.findById(1L).orElseThrow(BabbleNotFoundException::new);
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
        Room room = roomRepository.findById(1L).orElseThrow(BabbleNotFoundException::new);
        User guest = User.builder()
                .id(2L)
                .name("손님")
                .build();

        room.join(guest);
        assertThatThrownBy(() -> room.join(guest)).isInstanceOf(BabbleDuplicatedException.class);
    }
}
