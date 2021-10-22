package gg.babble.babble.controller.beta;

import gg.babble.babble.dto.response.IndexPageGameResponse;
import gg.babble.babble.service.GameService;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/beta/games")
@RestController
public class GameBetaController {

    private final GameService gameService;

    public GameBetaController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<IndexPageGameResponse>> findIndexPageGamesByName(@RequestParam(defaultValue = "") final String keyword, final Pageable pageable) {
        return ResponseEntity.ok(gameService.findSortedGamesByName(keyword, pageable));
    }
}
