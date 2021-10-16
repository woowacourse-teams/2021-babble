package gg.babble.babble.service;

import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.repository.SessionRepository;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.dto.response.EntryResponse;
import gg.babble.babble.dto.response.SessionsResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import javax.annotation.PostConstruct;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EntryService {

    private static final String CHANNEL_NAME = "users";

    private final SessionRepository sessionRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RoomService roomService;
    private final UserService userService;

    public EntryService(final SessionRepository sessionRepository,
                        final RedisTemplate<String, Object> redisTemplate,
                        final RoomService roomService,
                        final UserService userService) {
        this.sessionRepository = sessionRepository;
        this.redisTemplate = redisTemplate;
        this.roomService = roomService;
        this.userService = userService;
    }

    @Transactional
    public void enter(final Long roomId, final SessionRequest request) {
        Room room = roomService.findById(roomId);
        User user = userService.findById(request.getUserId());
        Session session = new Session(request.getSessionId(), user, room);

        sessionRepository.save(session);

        EntryResponse response = new EntryResponse(roomId, SessionsResponse.of(room));
        redisTemplate.convertAndSend(CHANNEL_NAME, response);
    }

    @Transactional
    public void exit(final String sessionId) {
        Session session = findBySessionId(sessionId);

        sessionRepository.deleteById(session.getId());

        Room room = session.getRoom();
        room.deleteSession(session);

        if (room.isEmpty()) {
            roomService.deleteRoom(room);
        }

        EntryResponse response = new EntryResponse(room.getId(), SessionsResponse.of(room));
        redisTemplate.convertAndSend(CHANNEL_NAME, response);
    }

    private Session findBySessionId(final String id) {
        return sessionRepository.findBySessionId(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s]는 존재하지 않는 세션 ID 입니다.", id)));
    }
}
