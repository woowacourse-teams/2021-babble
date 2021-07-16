package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

}
