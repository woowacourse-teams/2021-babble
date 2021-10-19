package gg.babble.babble.service;

import gg.babble.babble.domain.board.Board;
import gg.babble.babble.domain.repository.BoardRepository;
import gg.babble.babble.dto.request.board.BoardCreateRequest;
import gg.babble.babble.dto.request.board.BoardDeleteRequest;
import gg.babble.babble.dto.request.board.BoardUpdateRequest;
import gg.babble.babble.dto.response.BoardResponse;
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

    public List<BoardResponse> readAll() {
        List<Board> boards = boardRepository.findAll();

        return boards.stream()
            .map(BoardResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public BoardResponse update(final BoardUpdateRequest request) {
        Board board = find(request.getId());
        board.update(request.getTitle(), request.getContent(), request.getCategory());

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
