package gg.babble.babble.service;

import gg.babble.babble.domain.Room;
import gg.babble.babble.domain.User;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.dto.RoomRequestDto;
import gg.babble.babble.dto.RoomResponseDto;
import gg.babble.babble.dto.UserJoinRequest;
import gg.babble.babble.dto.UserListUpdateResponse;
import gg.babble.babble.dto.UserResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final GameService gameService;
    private final UserService userService;
    private final TagService tagService;
    private final SessionService sessionService;

    public RoomService(final RoomRepository roomRepository, final GameService gameService,
        final UserService userService, final TagService tagService, final SessionService sessionService) {
        this.roomRepository = roomRepository;
        this.gameService = gameService;
        this.userService = userService;
        this.tagService = tagService;
        this.sessionService = sessionService;
    }

    @Transactional
    public RoomResponseDto create(final RoomRequestDto roomRequestDto) {
        Room room = Room.builder()
                .game(gameService.findById(roomRequestDto.getGameId()))
                .tags(tagService.findById(roomRequestDto.getTags()))
                .build();
        return RoomResponseDto.from(roomRepository.save(room));
    }

    public RoomResponseDto findById(final Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new BabbleNotFoundException("존재하지 않는 방입니다."));
        return RoomResponseDto.from(room);
    }

    @Transactional
    public UserListUpdateResponse sendJoinRoom(final Long roomId, final UserJoinRequest request) {
        User user = userService.findById(request.getUserId());
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new BabbleNotFoundException("존재하지 않는 방 Id 입니다."));

        room.join(user);

        sessionService.create(room, request.getSessionId(), user);

        return UserListUpdateResponse.builder()
            .host(UserResponse.from(room.getHost()))
            .guests(getGuests(room))
            .build();
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

        return UserListUpdateResponse.builder()
            .host(UserResponse.from(room.getHost()))
            .guests(getGuests(room))
            .build();
    }
}
