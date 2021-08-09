package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.Game;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByDeletedFalse();

    List<Game> findByNameAndDeletedFalse(final String name);

    Optional<Game> findByIdAndDeletedFalse(final Long id);
}
