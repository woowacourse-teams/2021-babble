package gg.babble.babble;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.repository.TagRepository;
import gg.babble.babble.domain.repository.UserRepository;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("test")
@Transactional
@Component
public class DataLoader implements CommandLineRunner {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final RoomRepository roomRepository;

    public DataLoader(final GameRepository gameRepository, final UserRepository userRepository,
        final TagRepository tagRepository, final RoomRepository roomRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(final String... args) {
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
            .name("현구막")
            .build()
        );
        userRepository.save(User.builder()
            .id(4L)
            .name("포츈")
            .build()
        );
        userRepository.save(User.builder()
            .id(5L)
            .name("그루밍")
            .build()
        );
        userRepository.save(User.builder()
            .id(6L)
            .name("피터")
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
        List<Tag> tags = Arrays
            .asList(tagRepository.findById("실버").orElseThrow(BabbleNotFoundException::new),
                tagRepository.findById("2시간").orElseThrow(BabbleNotFoundException::new));

        Room room = Room.builder()
            .id(1L)
            .game(game)
            .tags(tags)
            .build();

        room.join(user);
        roomRepository.save(room);

        Room room2 = Room.builder()
            .id(2L)
            .game(game)
            .tags(tags)
            .build();
        roomRepository.save(room2);
    }
}
