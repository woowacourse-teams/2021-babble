package gg.babble.babble.controller;

import gg.babble.babble.dto.CreatedRoomResponse;
import gg.babble.babble.dto.FoundRoomResponse;
import gg.babble.babble.dto.RoomRequest;
import gg.babble.babble.service.RoomService;
import java.net.URI;
import javax.validation.Valid;
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
    public ResponseEntity<CreatedRoomResponse> createRoom(@Valid @RequestBody final RoomRequest request) {
        CreatedRoomResponse response = roomService.create(request);
        return ResponseEntity.created(URI.create("api/rooms/" + response.getRoomId()))
            .body(response);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<FoundRoomResponse> readRoom(@PathVariable final Long roomId) {
        FoundRoomResponse response = roomService.findById(roomId);
        return ResponseEntity.ok(response);
    }
}
