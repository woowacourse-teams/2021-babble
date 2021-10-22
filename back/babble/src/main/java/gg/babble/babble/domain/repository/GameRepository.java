package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.game.Game;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findByIdAndDeletedFalse(final Long id);

    @Query(value = "select game.id, game.name, game.deleted\n"
        + "from game\n"
        + "    left join alternative_game_name on alternative_game_name.deleted = false and game.id = alternative_game_name.game_id\n"
        + "    left join room on room.deleted = false and room.game_id = game.id\n"
        + "    left join session on session.deleted = false and session.room_id = room.id\n"
        + "where game.deleted = false\n"
        + "    and (alternative_game_name.name like %:keyword% or game.name like %:keyword%)\n"
        + "group by game.id\n"
        + "order by sum(case when session.id is null then 0 else 1 end) desc", nativeQuery = true)
    List<Game> findAllByKeyword(@Param("keyword") final String keyword, final Pageable pageable);
}
