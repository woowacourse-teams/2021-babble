package gg.babble.babble.service;

import gg.babble.babble.domain.Room;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.dto.*;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public RoomResponse create(final RoomRequest roomRequest) {
        Room room = Room.builder()
                .game(gameService.findById(roomRequest.getGameId()))
                .tags(tagService.findById(roomRequest.getTags()))
                .build();
        return RoomResponse.from(roomRepository.save(room));
    }

    public RoomResponse findById(final Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new BabbleNotFoundException("존재하지 않는 방입니다."));
        return RoomResponse.from(room);
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


        if (room.isEmpty()) {
            return UserListUpdateResponse.builder().build();
        }

        return UserListUpdateResponse.builder()
                .host(UserResponse.from(room.getHost()))
                .guests(getGuests(room))
                .build();
    }
}
