package gg.babble.babble.controller;

import gg.babble.babble.dto.request.RoomRequest;
import gg.babble.babble.dto.response.CreatedRoomResponse;
import gg.babble.babble.dto.response.FoundRoomResponse;
import gg.babble.babble.service.RoomService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return ResponseEntity.created(URI.create(String.format("api/rooms/%s", response.getRoomId())))
            .body(response);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<FoundRoomResponse> readRoom(@PathVariable final Long roomId) {
        FoundRoomResponse response = roomService.findById(roomId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FoundRoomResponse>> readRoomByTags(@RequestParam final Long gameId,
                                                                  @RequestParam(defaultValue = "") final List<Long> tagIds,
                                                                  final Pageable pageable) {
        return ResponseEntity.ok(roomService.findGamesByGameIdAndTagIds(gameId, tagIds, pageable));
    }
}
