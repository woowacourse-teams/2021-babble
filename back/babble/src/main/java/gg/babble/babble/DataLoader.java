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

    private static final int FIRST_DATA_INDEX = 0;
    private static final String LEAGUE_OF_LEGEND = "League Of Legend";
    private static final String OVERWATCH = "Overwatch";
    private static final String APEX_LEGEND = "Apex Legend";
    private static final String 루트 = "루트";
    private static final String 와일더 = "와일더";
    private static final String 현구막 = "현구막";
    private static final String 포츈 = "포츈";
    private static final String 그루밍 = "그루밍";
    private static final String 피터 = "피터";
    private static final String 실버 = "실버";
    private static final String _2시간 = "2시간";
    private static final String 솔로랭크 = "솔로랭크";

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final RoomRepository roomRepository;

    public DataLoader(final GameRepository gameRepository, final UserRepository userRepository, final TagRepository tagRepository,
                      final RoomRepository roomRepository) {
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
        gameRepository.save(new Game(LEAGUE_OF_LEGEND));
        gameRepository.save(new Game(OVERWATCH));
        gameRepository.save(new Game(APEX_LEGEND));
    }

    private void prepareDummyUsers() {
        userRepository.save(new User(루트));
        userRepository.save(new User(와일더));
        userRepository.save(new User(현구막));
        userRepository.save(new User(포츈));
        userRepository.save(new User(그루밍));
        userRepository.save(new User(피터));
    }

    private void prepareDummyTags() {
        tagRepository.save(new Tag(실버));
        tagRepository.save(new Tag(_2시간));
        tagRepository.save(new Tag(솔로랭크));
    }

    private void prepareDummyRoom() {
        Game game = gameRepository.findByName(LEAGUE_OF_LEGEND).get(FIRST_DATA_INDEX);
        User user = userRepository.findByName(루트).get(FIRST_DATA_INDEX);
        List<Tag> tags = Arrays
            .asList(tagRepository.findById(실버).orElseThrow(BabbleNotFoundException::new),
                tagRepository.findById(_2시간).orElseThrow(BabbleNotFoundException::new));

        Room room = new Room(game, tags);

        room.join(user);
        roomRepository.save(room);
    }
}
