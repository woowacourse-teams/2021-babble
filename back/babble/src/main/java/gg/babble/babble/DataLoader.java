package gg.babble.babble;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private GameRepository gameRepository;

    public DataLoader(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Game leagueOfLegend = gameRepository.save(Game.builder()
                .id(1L)
                .name("League Of Legend")
                .build()
        );
        Game overwatch = gameRepository.save(Game.builder()
                .id(2L)
                .name("Overwatch")
                .build()
        );
        Game apexLegend = gameRepository.save(Game.builder()
                .id(3L)
                .name("Apex Legend")
                .build()
        );
    }
}
