package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.service.GameService;
import gg.babble.babble.service.TagService;
import gg.babble.babble.service.UserService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class RoomRepositoryTest {

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
        Game game = gameService.findById(1L);
        User user = userService.findById(1L);
        List<Tag> tags = Arrays.asList(tagService.findById("실버"),
            tagService.findById("2시간"));

        Room room = roomRepository.save(Room.builder()
                .game(game)
                .host(user)
                .tags(tags).build());
        roomRepository.flush();

        assertThat(roomRepository.existsById(room.getId())).isTrue();
    }
}
