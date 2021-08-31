package gg.babble.babble.service;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.Games;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.dto.request.GameRequest;
import gg.babble.babble.dto.response.GameImageResponse;
import gg.babble.babble.dto.response.GameWithImageResponse;
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

    public List<IndexPageGameResponse> findSortedGames() {
        Games games = new Games(gameRepository.findByDeletedFalse());
        games.sortedByHeadCount();

        return IndexPageGameResponse.listFrom(games);
    }

    public GameImageResponse findGameImageById(final Long gameId) {
        return GameImageResponse.from(findGameById(gameId));
    }

    public GameWithImageResponse findGame(final Long gameId) {
        return GameWithImageResponse.from(findGameById(gameId));
    }

    public Game findGameById(final Long id) {
        return gameRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("존재하지 않는 게임 Id(%d) 입니다.", id)));
    }
    
    public List<GameImageResponse> findAllGameImages() {
        return gameRepository.findByDeletedFalse()
            .stream()
            .map(GameImageResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public GameWithImageResponse insertGame(final GameRequest request) {
        Game game = gameRepository.save(request.toEntity());

        return GameWithImageResponse.from(game);
    }

    @Transactional
    public GameWithImageResponse updateGame(final Long gameId, final GameRequest request) {
        Game game = findGameById(gameId);
        game.update(request.toEntity());

        return GameWithImageResponse.from(game);
    }

    @Transactional
    public void deleteGame(final Long gameId) {
        Game game = findGameById(gameId);
        game.delete();
    }
}
