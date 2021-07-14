package gg.babble.babble.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class RoomRepository {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    private TestEntityManager testEntityManager;

    @DisplayName("생성한 방을 저장한다.")
    @Test
    void saveTest() {
        Room room = roomRepository.save(Room.builder()
                .game(gameRepository.findById(1L))
                .host(userRepository.findById(1L))
                .tags(Arrays.asList(tagRepository.findById("실버"),
                        tagRepository.findById("2시간"))
                )
        );
        testEntityManager.flush();

        assertThat(roomRepository.findById(room.getId()));
    }
}
