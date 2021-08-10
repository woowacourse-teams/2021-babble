package gg.babble.babble.service;

import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.repository.SessionRepository;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.dto.response.SessionsResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;
    private final RoomService roomService;
    private final UserService userService;

    public SessionService(final SessionRepository sessionRepository, final RoomService roomService, final UserService userService) {
        this.sessionRepository = sessionRepository;
        this.roomService = roomService;
        this.userService = userService;
    }

    public Long findRoomIdBySessionId(final String sessionId) {
        Session session = findBySessionId(sessionId);
        Room room = session.getRoom();

        return room.getId();
    }

    @Transactional
    public SessionsResponse create(final Long roomId, final SessionRequest request) {
        Room room = roomService.findRoomOrElseThrow(roomId);
        User user = userService.findById(request.getUserId());

        Session session = sessionRepository.save(new Session(request.getSessionId(), room, user));
        session.userEnterRoom();

        return SessionsResponse.of(room.getHost(), room.getGuests());
    }

    @Transactional
    public SessionsResponse delete(final String sessionId) {
        Session session = findBySessionId(sessionId);
        Room room = session.getRoom();

        session.userExitRoom();

        if (room.isEmpty()) {
            return SessionsResponse.empty();
        }
        return SessionsResponse.of(room.getHost(), room.getGuests());
    }

    private Session findBySessionId(final String id) {
        return sessionRepository.findBySessionIdAndDeletedFalse(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s]는 존재하지 않는 세션 ID 입니다.", id)));
    }
}
