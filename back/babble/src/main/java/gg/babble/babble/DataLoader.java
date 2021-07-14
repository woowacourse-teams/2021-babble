package gg.babble.babble;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.GameRepository;
import gg.babble.babble.domain.User;
import gg.babble.babble.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public DataLoader(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
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
}
