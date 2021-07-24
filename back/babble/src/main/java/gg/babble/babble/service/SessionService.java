package gg.babble.babble.service;

import gg.babble.babble.domain.Room;
import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.domain.repository.SessionRepository;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(final SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void create(final Room room, final String sessionId, final User user) {
        Session session = Session.builder()
                .room(room)
                .sessionId(sessionId)
                .user(user)
                .build();

        sessionRepository.save(session);
    }

    public Room findRoomBySessionId(final String sessionId) {
        Session session = sessionRepository.findSessionBySessionId(sessionId)
                .orElseThrow(() -> new BabbleNotFoundException("존재하지 않는 세션 Id 입니다."));

        return session.getRoom();
    }

    public User findUserBySessionId(final String sessionId) {
        Session session = sessionRepository.findSessionBySessionId(sessionId)
                .orElseThrow(() -> new BabbleNotFoundException("존재하지 않는 세션 Id 입니다."));

        return session.getUser();
    }

    public void delete(final String sessionId) {
        sessionRepository.deleteBySessionId(sessionId);
    }
}
