package gg.babble.babble;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.Room;
import gg.babble.babble.domain.Tag;
import gg.babble.babble.domain.User;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.repository.TagRepository;
import gg.babble.babble.domain.repository.UserRepository;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final RoomRepository roomRepository;

    public DataLoader(GameRepository gameRepository, UserRepository userRepository, TagRepository tagRepository, RoomRepository roomRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) {
        prepareDummyGames();
        prepareDummyUsers();
        prepareDummyTags();
        prepareDummyRoom();
    }

    private void prepareDummyGames() {
        gameRepository.save(Game.builder()
            .id(1L)
            .name("League Of Legend")
            .build()
        );
        gameRepository.save(Game.builder()
            .id(2L)
            .name("Overwatch")
            .build()
        );
        gameRepository.save(Game.builder()
            .id(3L)
            .name("Apex Legend")
            .build()
        );
    }

    private void prepareDummyUsers() {
        userRepository.save(User.builder()
            .id(1L)
            .name("루트")
            .build()
        );
        userRepository.save(User.builder()
            .id(2L)
            .name("와일더")
            .build()
        );
        userRepository.save(User.builder()
            .id(3L)
            .name("포비")
            .build()
        );
    }

    private void prepareDummyTags() {
        tagRepository.save(Tag.builder()
            .name("실버")
            .build()
        );
        tagRepository.save(Tag.builder()
            .name("2시간")
            .build()
        );
        tagRepository.save(Tag.builder()
            .name("솔로랭크")
            .build()
        );
    }

    private void prepareDummyRoom() {
        Game game = gameRepository.findById(1L).orElseThrow(BabbleNotFoundException::new);
        User user = userRepository.findById(1L).orElseThrow(BabbleNotFoundException::new);
        List<Tag> tags = Arrays.asList(tagRepository.findById("실버").orElseThrow(BabbleNotFoundException::new),
                tagRepository.findById("2시간").orElseThrow(BabbleNotFoundException::new));
        Room room = Room.builder()
                .id(1L)
                .game(game)
                .host(user)
                .tags(tags)
                .build();

        roomRepository.save(room);
    }
}
