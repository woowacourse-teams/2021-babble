package gg.babble.babble;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.repository.TagRepository;
import gg.babble.babble.domain.repository.UserRepository;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
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
    private static final String LEAGUE_OF_LEGENDS = "League Of Legends";
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
        gameRepository.save(new Game(LEAGUE_OF_LEGENDS, "https://static-cdn.jtvnw.net/ttv-boxart/League%20of%20Legends-1080x1436.jpg"));
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
        userRepository.save(new User("0"));
        userRepository.save(new User("1"));
        userRepository.save(new User("2"));
        userRepository.save(new User("3"));
        userRepository.save(new User("4"));
        userRepository.save(new User("5"));
        userRepository.save(new User("6"));
        userRepository.save(new User("7"));
        userRepository.save(new User("8"));
        userRepository.save(new User("9"));
        userRepository.save(new User("10"));
        userRepository.save(new User("11"));
        userRepository.save(new User("12"));
        userRepository.save(new User("13"));
        userRepository.save(new User("14"));
        userRepository.save(new User("15"));
        userRepository.save(new User("16"));
        userRepository.save(new User("17"));
        userRepository.save(new User("18"));
        userRepository.save(new User("19"));
    }

    private void prepareDummyTags() {
        tagRepository.save(new Tag(실버));
        tagRepository.save(new Tag(_2시간));
        tagRepository.save(new Tag(솔로랭크));
    }

    private void prepareDummyRoom() {
        for (int i = 0; i < 20; i++) {
            Room room = createAndJoinRoom(i);
            roomRepository.save(room);
        }
    }

    private Room createAndJoinRoom(int index) {
        Game game = gameRepository.findByName(LEAGUE_OF_LEGENDS).get(FIRST_DATA_INDEX);
        User user = userRepository.findByNickname(Integer.toString(index)).get(FIRST_DATA_INDEX);
        List<Tag> tags = Arrays
            .asList(tagRepository.findByName(실버).get(0),
                tagRepository.findByName(_2시간).get(0));

        Room room = new Room(game, tags, new MaxHeadCount(4));

        room.join(user);
        return room;
    }
}
