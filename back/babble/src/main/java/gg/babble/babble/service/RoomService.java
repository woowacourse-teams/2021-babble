package gg.babble.babble.service;

import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.RoomRequest;
import gg.babble.babble.dto.request.UserJoinRequest;
import gg.babble.babble.dto.response.CreatedRoomResponse;
import gg.babble.babble.dto.response.FoundRoomResponse;
import gg.babble.babble.dto.response.UserListUpdateResponse;
import gg.babble.babble.dto.response.UserResponse;
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
        Room room = new Room(gameService.findById(request.getGameId()), tagService.findById(request.getTags()),
            new MaxHeadCount(request.getMaxHeadCount()));
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
            return roomRepository.findAllByGameId(gameId, pageable);
        }
        Set<Long> distinctTagIds = new HashSet<>(tagIds);
        return roomRepository.findAllByGameIdAndTagIds(gameId, distinctTagIds, (long) distinctTagIds.size(), pageable);
    }

    private Room findRoomOrElseThrow(final Long id) {
        return roomRepository.findById(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("존재하지 않는 게임 Id(%d) 입니다.", id)));
    }

    @Transactional
    public UserListUpdateResponse sendJoinRoom(final Long roomId, final UserJoinRequest request) {
        User user = userService.findById(request.getUserId());
        Room room = findRoomOrElseThrow(roomId);

        room.join(user);

        sessionService.create(room, request.getSessionId(), user);

        return new UserListUpdateResponse(UserResponse.from(room.getHost()), getGuests(room));
    }

    private List<UserResponse> getGuests(final Room room) {
        return room.getGuests()
            .stream()
            .map(UserResponse::from)
            .collect(Collectors.toList());
    }

    public Long findRoomIdBySessionId(final String sessionId) {
        Room room = sessionService.findRoomBySessionId(sessionId);
        return room.getId();
    }

    @Transactional
    public UserListUpdateResponse sendExitRoom(final String sessionId) {
        Room room = sessionService.findRoomBySessionId(sessionId);
        User user = sessionService.findUserBySessionId(sessionId);

        room.leave(user);
        sessionService.delete(sessionId);

        if (room.isEmpty()) {
            return UserListUpdateResponse.empty();
        }

        return new UserListUpdateResponse(UserResponse.from(room.getHost()), getGuests(room));
    }

    public boolean isFullRoom(final Long id) {
        Room room = findRoomOrElseThrow(id);
        return room.isFull();
    }
}
