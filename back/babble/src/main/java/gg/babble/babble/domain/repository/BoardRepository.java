package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.board.Board;
import gg.babble.babble.domain.board.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByIdAndDeletedFalse(final Long id);

    List<Board> findByCategory(Category category);

    @Query("select b "
        + "from Board b "
        + "where b.deleted = false and b.post.title like %?1% "
        + "order by b.createdAt desc ")
    List<Board> findAllAndTitleContainsKeywordAndDeletedFalse(final String keyword);

    @Query("select b "
        + "from Board b "
        + "where b.deleted = false and (b.post.title like %?1% or b.post.content like %?1%) "
        + "order by b.createdAt desc ")
    List<Board> findAllAndTitleAndContentContainsKeywordAndDeletedFalse(final String keyword);

    @Query("select b "
        + "from Board b "
        + "where b.deleted = false and b.account.nickname like %?1% "
        + "order by b.createdAt desc ")
    List<Board> findAllAndAuthorContainsKeywordAndDeletedFalse(final String keyword);

    @Query("select b "
        + "from Board b "
        + "where b.deleted = false and (b.post.title like %?1% or b.post.content like %?1% or b.account.nickname like %?1%) "
        + "order by b.createdAt desc ")
    List<Board> findAllAndContainsKeywordAndDeletedFalse(final String keyword);
}
