package gg.babble.babble.service;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.Games;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.dto.response.GameImageResponse;
import gg.babble.babble.dto.response.IndexPageGameResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
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

    public List<IndexPageGameResponse> findSortedGames() {
        Games games = new Games(gameRepository.findAll());
        games.sortedByHeadCount();

        return IndexPageGameResponse.listFrom(games);
    }

    public GameImageResponse findGameImageById(final Long gameId) {
        return GameImageResponse.from(findById(gameId));
    }

    public List<GameImageResponse> findAllGameImages() {
        return gameRepository.findAll()
            .stream()
            .map(GameImageResponse::from)
            .collect(Collectors.toList());
    }
}
