package gg.babble.babble.service;

import gg.babble.babble.domain.board.Board;
import gg.babble.babble.domain.board.BoardSearchType;
import gg.babble.babble.domain.board.Category;
import gg.babble.babble.domain.repository.BoardRepository;
import gg.babble.babble.dto.request.board.BoardCategoryRequest;
import gg.babble.babble.dto.request.board.BoardCreateRequest;
import gg.babble.babble.dto.request.board.BoardDeleteRequest;
import gg.babble.babble.dto.request.board.BoardSearchRequest;
import gg.babble.babble.dto.request.board.BoardUpdateRequest;
import gg.babble.babble.dto.response.BoardResponse;
import gg.babble.babble.dto.response.BoardSearchResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public BoardResponse create(final BoardCreateRequest request) {
        Board board = request.toEntity();
        boardRepository.save(board);

        return BoardResponse.from(board);
    }

    public BoardSearchResponse search(final BoardSearchRequest request) {
        BoardSearchType boardSearchType = BoardSearchType.of(request.getType());

        List<Board> boards = boardSearchType.compose(boardRepository, request.getKeyword());

        List<BoardResponse> result = boards.stream()
            .map(BoardResponse::from)
            .collect(Collectors.toList());

        return new BoardSearchResponse(result, request.getKeyword(), request.getType());
    }

    public BoardResponse findById(final Long id) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s]번 게시글은 존재하지 않습니다.", id)));

        return BoardResponse.from(board);
    }

    public List<BoardResponse> findByCategory(final BoardCategoryRequest request) {
        Category category = Category.of(request.getCategory());
        List<Board> boards = boardRepository.findByCategory(category);

        return boards.stream()
            .map(BoardResponse::from)
            .collect(Collectors.toList());
    }

    public List<BoardResponse> findAll() {
        List<Board> boards = boardRepository.findAll();

        return boards.stream()
            .map(BoardResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public BoardResponse update(final BoardUpdateRequest request) {
        Board board = find(request.getId());
        board.update(request.getTitle(), request.getContent(), request.getCategory(), request.getPassword());

        return BoardResponse.from(board);
    }

    private Board find(final Long id) {
        return boardRepository.findById(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s]번 게시글은 존재하지 않습니다.", id)));
    }

    @Transactional
    public void delete(final BoardDeleteRequest request) {
        Board board = find(request.getId());
        board.delete(request.getPassword());
        boardRepository.delete(board);
    }
}
