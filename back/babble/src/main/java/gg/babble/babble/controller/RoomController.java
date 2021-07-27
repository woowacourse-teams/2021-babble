package gg.babble.babble.controller;

import gg.babble.babble.dto.RoomRequest;
import gg.babble.babble.dto.RoomResponse;
import gg.babble.babble.service.RoomService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/rooms")
@RestController
public class RoomController {

    private final RoomService roomService;

    public RoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody final RoomRequest roomRequest) {
        RoomResponse roomResponse = roomService.create(roomRequest);
        return ResponseEntity.created(URI.create("api/rooms/" + roomResponse.getRoomId()))
            .body(roomResponse);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> readRoom(@PathVariable final Long roomId) {
        RoomResponse roomResponse = roomService.findById(roomId);
        return ResponseEntity.ok(roomResponse);
    }
}
