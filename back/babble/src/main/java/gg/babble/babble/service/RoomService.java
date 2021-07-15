package gg.babble.babble.service;

import gg.babble.babble.domain.Room;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.dto.RoomRequestDto;
import gg.babble.babble.dto.RoomResponseDto;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final GameService gameService;
    private final UserService userService;
    private final TagService tagService;

    public RoomService(RoomRepository roomRepository, GameService gameService,
        UserService userService, TagService tagService) {
        this.roomRepository = roomRepository;
        this.gameService = gameService;
        this.userService = userService;
        this.tagService = tagService;
    }

    public RoomResponseDto create(RoomRequestDto roomRequestDto) {
        Room room = Room.builder()
            .game(gameService.findById(roomRequestDto.getGameId()))
            .host(userService.findById(roomRequestDto.getHostId()))
            .tags(tagService.findById(roomRequestDto.getTags()))
            .build();
        return RoomResponseDto.from(roomRepository.save(room));
    }

    public RoomResponseDto findById(Long id) {
        Room room = roomRepository.findById(id)
            .orElseThrow(() -> new BabbleNotFoundException("존재하지 않는 방입니다."));
        return RoomResponseDto.from(room);
    }
}
