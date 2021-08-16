package gg.babble.babble.service;

import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.repository.SessionRepository;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.dto.response.ExitResponse;
import gg.babble.babble.dto.response.SessionsResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EnterExitService {

    private final SessionRepository sessionRepository;
    private final RoomService roomService;
    private final UserService userService;

    public EnterExitService(final SessionRepository sessionRepository, final RoomService roomService, final UserService userService) {
        this.sessionRepository = sessionRepository;
        this.roomService = roomService;
        this.userService = userService;
    }

    @Transactional
    public SessionsResponse enter(final Long roomId, final SessionRequest request) {
        Room room = roomService.findById(roomId);
        User user = userService.findById(request.getUserId());
        Session session = new Session(request.getSessionId(), user, room);

        sessionRepository.save(session);

        return SessionsResponse.of(room.getHost(), room.getGuests());
    }

    @Transactional
    public ExitResponse exit(final String sessionId) {
        Session session = findBySessionId(sessionId);

        session.delete();

        if (session.getRoom().isDeleted()) {
            return new ExitResponse(session.getRoom().getId(), SessionsResponse.empty());
        }
        return new ExitResponse(session.getRoom().getId(), SessionsResponse.of(session.getRoom().getHost(), session.getRoom().getGuests()));
    }

    private Session findBySessionId(final String id) {
        return sessionRepository.findBySessionIdAndDeletedFalse(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s]는 존재하지 않는 세션 ID 입니다.", id)));
    }
}
