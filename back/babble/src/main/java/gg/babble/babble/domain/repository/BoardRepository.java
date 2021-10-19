package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
