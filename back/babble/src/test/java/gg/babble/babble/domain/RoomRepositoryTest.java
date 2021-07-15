package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.repository.RoomRepository;
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
        assertThat(room.isPresent()).isTrue();
        assertThat(room.get().getCreatedDate()).isNotNull();
        assertThat(room.get().getGame())
            .usingRecursiveComparison()
            .isEqualTo(Game.builder()
                .id(1L)
                .name("League Of Legend")
                .build());
        assertThat(room.get().getHost())
            .usingRecursiveComparison()
            .isEqualTo(User.builder()
                .id(1L)
                .name("루트")
                .build());
        assertThat(room.get().getTags())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList("실버", "2시간"));
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
}
