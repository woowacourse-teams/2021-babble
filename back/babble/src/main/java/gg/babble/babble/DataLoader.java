package gg.babble.babble;

import gg.babble.babble.domain.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public DataLoader(GameRepository gameRepository, UserRepository userRepository, TagRepository tagRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void run(String... args) {
        prepareDummyGames();
        prepareDummyUsers();
        prepareDummyTags();
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
}
