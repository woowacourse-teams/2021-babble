package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.Session;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query(value = "select s "
        + "from Session s join fetch s.user join fetch s.room "
        + "where s.sessionId = ?1")
    Optional<Session> findBySessionId(final String sessionId);
}
