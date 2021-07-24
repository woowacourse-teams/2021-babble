package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.Session;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findSessionBySessionId(final String sessionId);

    void deleteBySessionId(final String sessionId);
}
