package gg.babble.babble.service;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GameService {

    private final GameRepository gameRepository;

    public GameService(final GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game findById(final Long id) {
        return gameRepository.findById(id)
            .orElseThrow(() -> new BabbleNotFoundException("존재하지 않는 게임 Id 입니다."));
    }

    public List<Game> findByName(final String name) {
        return gameRepository.findByName(name);
    }
}
