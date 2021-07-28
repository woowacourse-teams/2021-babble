package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleNotFoundException;
import gg.babble.babble.service.GameService;
import gg.babble.babble.service.TagService;
import gg.babble.babble.service.UserService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoomRepositoryTest extends ApplicationTest {

    private static final String LEAGUE_OF_LEGENDS = "League Of Legends";
    private static final String 루트 = "루트";
    private static final String 실버 = "실버";
    private static final String _2시간 = "2시간";

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @DisplayName("생성한 방을 저장한다.")
    @Test
    void saveTest() {
        Room room = saveRoom();
        roomRepository.flush();

        assertThat(roomRepository.existsById(room.getId())).isTrue();
    }

    private Room saveRoom() {
        Game game = gameService.findByName(LEAGUE_OF_LEGENDS).get(0);
        User user = userService.findByNickname(루트).get(0);
        List<Tag> tags = Arrays.asList(tagService.findById(실버),
            tagService.findById(_2시간));

        Room room = roomRepository.save(new Room(game, tags, new MaxHeadCount(4)));

        room.join(user);

        return room;
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

        assertThat(roomRepository.findById(room.getId())
            .orElseThrow(BabbleNotFoundException::new)
            .isDeleted())
            .isTrue();
    }
}
