package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.Room;
import gg.babble.babble.domain.Tag;
import gg.babble.babble.domain.User;
import gg.babble.babble.service.GameService;
import gg.babble.babble.service.TagService;
import gg.babble.babble.service.UserService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

    @DisplayName("방에 모든 유저가 나갈 경우 방을 삭제한다.")
    @Test
    void removeRoomWithoutUsers() {
        Room room = saveRoom();
        room.leave(room.getHost());
        roomRepository.flush();

        assertThat(roomRepository.existsById(room.getId())).isFalse();
    }
}
