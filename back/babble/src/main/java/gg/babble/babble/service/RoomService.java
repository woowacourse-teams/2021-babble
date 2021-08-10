package gg.babble.babble.service;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.RoomRequest;
import gg.babble.babble.dto.request.UserJoinRequest;
import gg.babble.babble.dto.response.CreatedRoomResponse;
import gg.babble.babble.dto.response.FoundRoomResponse;
import gg.babble.babble.dto.response.UserListUpdateResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoomService {

    private static final int PAGE_SIZE = 16;

    private final RoomRepository roomRepository;
    private final GameService gameService;
    private final UserService userService;
    private final TagService tagService;
    private final SessionService sessionService;

    public RoomService(final RoomRepository roomRepository, final GameService gameService, final UserService userService, final TagService tagService,
                       final SessionService sessionService) {
        this.roomRepository = roomRepository;
        this.gameService = gameService;
        this.userService = userService;
        this.tagService = tagService;
        this.sessionService = sessionService;
    }

    @Transactional
    public CreatedRoomResponse create(final RoomRequest request) {
        Game game = gameService.findGameById(request.getGameId());
        List<Tag> tags = tagService.findAllById(request.getTags());
        MaxHeadCount maxHeadCount = new MaxHeadCount(request.getMaxHeadCount());

        Room room = new Room(game, tags, maxHeadCount);

        return CreatedRoomResponse.from(roomRepository.save(room));
    }

    public FoundRoomResponse findById(final Long id) {
        return FoundRoomResponse.from(findRoomOrElseThrow(id));
    }

    public List<FoundRoomResponse> findGamesByGameIdAndTagIds(final Long gameId, final List<Long> tagIds, final Pageable pageable) {
        return findByGameIdAndTagIdsInRepository(gameId, tagIds, pageable).stream()
            .map(FoundRoomResponse::from)
            .collect(Collectors.toList());
    }

    private List<Room> findByGameIdAndTagIdsInRepository(final Long gameId, final List<Long> tagIds, final Pageable pageable) {
        if (tagIds.isEmpty()) {
            return roomRepository.findAllByGameIdAndDeletedFalse(gameId, pageable);
        }

        Set<Long> distinctTagIds = new HashSet<>(tagIds);

        return roomRepository.findAllByGameIdAndTagIdsAndDeletedFalse(gameId, distinctTagIds, (long) distinctTagIds.size(), pageable);
    }

    @Transactional
    public UserListUpdateResponse sendEnterRoom(final Long roomId, final UserJoinRequest request) {
        Room room = findRoomOrElseThrow(roomId);
        User user = userService.findById(request.getUserId());

        sessionService.userEnterRoom(request.getSessionId(), room, user);

        return UserListUpdateResponse.of(room.getHost(), room.getGuests());
    }

    private Room findRoomOrElseThrow(final Long id) {
        return roomRepository.findById(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%d]는 존재하지 않는 방 ID 입니다.", id)));
    }

    public Long findRoomIdBySessionId(final String sessionId) {
        Room room = sessionService.findRoomBySessionId(sessionId);

        return room.getId();
    }

    @Transactional
    public UserListUpdateResponse sendExitRoom(final String sessionId) {
        Room room = sessionService.findRoomBySessionId(sessionId);
        User user = sessionService.findUserBySessionId(sessionId);

        sessionService.userExitRoom(sessionId, room, user);

        if (room.isEmpty()) {
            return UserListUpdateResponse.empty();
        }

        return UserListUpdateResponse.of(room.getHost(), room.getGuests());
    }

    public boolean isFullRoom(final Long id) {
        Room room = findRoomOrElseThrow(id);

        return room.isFull();
    }
}
