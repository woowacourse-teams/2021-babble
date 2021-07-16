package gg.babble.babble.controller;

import gg.babble.babble.dto.RoomRequestDto;
import gg.babble.babble.dto.RoomResponseDto;
import gg.babble.babble.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping(value = "/api/rooms")
@RestController
public class RoomController {

    private final RoomService roomService;

    public RoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody final RoomRequestDto roomRequestDto) {
        RoomResponseDto roomResponseDto = roomService.create(roomRequestDto);
        return ResponseEntity.created(URI.create("api/rooms/" + roomResponseDto.getRoomId()))
                .body(roomResponseDto);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponseDto> readRoom(@PathVariable final Long roomId) {
        RoomResponseDto roomResponseDto = roomService.findById(roomId);
        return ResponseEntity.ok(roomResponseDto);
    }
}
