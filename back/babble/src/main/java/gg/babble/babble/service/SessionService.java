package gg.babble.babble.service;

import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.repository.SessionRepository;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(final SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public void create(final Room room, final String sessionId, final User user) {
        Session session = new Session(sessionId, room, user);

        sessionRepository.save(session);
    }

    public Room findRoomBySessionId(final String sessionId) {
        Session session = findSessionOrElseThrow(sessionId);

        return session.getRoom();
    }

    private Session findSessionOrElseThrow(final String sessionId) {
        return sessionRepository.findSessionBySessionId(sessionId)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("존재하지 않는 세션 Id(%s) 입니다.", sessionId)));
    }

    public User findUserBySessionId(final String sessionId) {
        Session session = findSessionOrElseThrow(sessionId);

        return session.getUser();
    }

    @Transactional
    public void delete(final String sessionId) {
        sessionRepository.deleteBySessionId(sessionId);
    }
}
