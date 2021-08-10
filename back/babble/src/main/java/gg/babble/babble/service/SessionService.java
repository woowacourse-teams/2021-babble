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
    public void userEnterRoom(final String sessionId, final Room room, final User user) {
        Session session = sessionRepository.save(new Session(sessionId, room, user));
    }

    @Transactional
    public void userExitRoom(final String sessionId, final Room room, final User user) {
        Session session = findSessionBySessionId(sessionId);
        session.delete();
    }

    private Session findSessionBySessionId(final String id) {
        return sessionRepository.findBySessionIdAndDeletedFalse(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s]는 존재하지 않는 세션 ID 입니다.", id)));
    }

    public Room findRoomBySessionId(final String sessionId) {
        Session session = findSessionOrElseThrow(sessionId);

        return session.getRoom();
    }

    public User findUserBySessionId(final String sessionId) {
        Session session = findSessionOrElseThrow(sessionId);

        return session.getUser();
    }

    @Transactional
    public void deleteSessionBySessionId(final String sessionId) {
        Session session = findSessionOrElseThrow(sessionId);

        session.delete();
    }

    private Session findSessionOrElseThrow(final String sessionId) {
        return sessionRepository.findBySessionIdAndDeletedFalse(sessionId)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s]는 존재하지 않는 sessionId 입니다.", sessionId)));
    }
}
