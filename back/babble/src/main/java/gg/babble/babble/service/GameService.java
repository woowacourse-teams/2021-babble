package gg.babble.babble.service;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game findById(Long id) {
        return gameRepository.findById(id)
            .orElseThrow(() -> new BabbleNotFoundException("존재하지 않는 게임 Id 입니다."));
    }
}
