package gg.babble.babble.controller;

import gg.babble.babble.dto.response.GameImageResponse;
import gg.babble.babble.dto.response.GameWithImageResponse;
import gg.babble.babble.dto.response.IndexPageGameResponse;
import gg.babble.babble.service.GameService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/games")
@RestController
public class GameController {

    private final GameService gameService;

    public GameController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<IndexPageGameResponse>> findIndexPageGames() {
        return ResponseEntity.ok(gameService.findSortedGames());
    }

    @GetMapping(value = "/{gameId}")
    public ResponseEntity<GameWithImageResponse> findGame(@PathVariable final Long gameId) {
        return ResponseEntity.ok(gameService.findGame(gameId));
    }

    @GetMapping(value = "/{gameId}/images")
    public ResponseEntity<GameImageResponse> findGameImage(@PathVariable final Long gameId) {
        return ResponseEntity.ok(gameService.findGameImageById(gameId));
    }

    @GetMapping(value = "/images")
    public ResponseEntity<List<GameImageResponse>> findAllGameImages() {
        return ResponseEntity.ok(gameService.findAllGameImages());
    }
}
