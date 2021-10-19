package gg.babble.babble.controller;

import gg.babble.babble.dto.request.board.BoardCreateRequest;
import gg.babble.babble.dto.request.board.BoardDeleteRequest;
import gg.babble.babble.dto.request.board.BoardUpdateRequest;
import gg.babble.babble.dto.response.BoardResponse;
import gg.babble.babble.service.BoardService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/board")
@RestController
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<BoardResponse> post(@RequestBody final BoardCreateRequest request) {
        BoardResponse response = boardService.create(request);
        return ResponseEntity.created(URI.create(String.format("api/board/%s", response.getId())))
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> read() {
        List<BoardResponse> responses = boardService.readAll();
        return ResponseEntity.ok(responses);
    }

    @PutMapping
    public ResponseEntity<BoardResponse> update(@RequestBody final BoardUpdateRequest request) {
        BoardResponse response = boardService.update(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody final BoardDeleteRequest request) {
        boardService.delete(request);
        return ResponseEntity.noContent().build();
    }
}
