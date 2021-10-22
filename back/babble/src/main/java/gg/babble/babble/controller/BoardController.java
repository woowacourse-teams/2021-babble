package gg.babble.babble.controller;

import gg.babble.babble.dto.request.board.BoardCategoryRequest;
import gg.babble.babble.dto.request.board.BoardCreateRequest;
import gg.babble.babble.dto.request.board.BoardDeleteRequest;
import gg.babble.babble.dto.request.board.BoardSearchRequest;
import gg.babble.babble.dto.request.board.BoardUpdateRequest;
import gg.babble.babble.dto.response.BoardResponse;
import gg.babble.babble.dto.response.BoardSearchResponse;
import gg.babble.babble.service.BoardService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> read(@PathVariable final Long boardId) {
        BoardResponse response = boardService.findById(boardId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<BoardSearchResponse> search(@RequestBody final BoardSearchRequest request) {
        BoardSearchResponse response = boardService.search(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category")
    public ResponseEntity<List<BoardResponse>> searchByCategory(@RequestBody final BoardCategoryRequest request) {
        List<BoardResponse> response = boardService.findByCategory(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> readAll() {
        List<BoardResponse> responses = boardService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PutMapping
    public ResponseEntity<BoardResponse> update(@RequestBody final BoardUpdateRequest request) {
        BoardResponse response = boardService.update(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{boardId}/like")
    public ResponseEntity<BoardResponse> like(@PathVariable final Long boardId) {
        BoardResponse response = boardService.like(boardId);
        return ResponseEntity.created(URI.create(String.format("api/board/%s", response.getId())))
            .body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody final BoardDeleteRequest request) {
        boardService.delete(request);
        return ResponseEntity.noContent().build();
    }
}
