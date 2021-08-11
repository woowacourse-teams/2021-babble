package gg.babble.babble.service;

import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.dto.request.RoomRequest;
import gg.babble.babble.dto.response.CreatedRoomResponse;
import gg.babble.babble.dto.response.FoundRoomResponse;
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
    private final TagService tagService;

    public RoomService(final RoomRepository roomRepository, final GameService gameService, final TagService tagService) {
        this.roomRepository = roomRepository;
        this.gameService = gameService;
        this.tagService = tagService;
    }

    @Transactional
    public CreatedRoomResponse create(final RoomRequest request) {
        Game game = gameService.findGameById(request.getGameId());
        List<Tag> tags = tagService.findAllById(request.getTags());
        MaxHeadCount maxHeadCount = new MaxHeadCount(request.getMaxHeadCount());

        Room room = new Room(game, tags, maxHeadCount);

        return CreatedRoomResponse.from(roomRepository.save(room));
    }

    public FoundRoomResponse findRoomById(final Long id) {
        return FoundRoomResponse.from(findById(id));
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

    public boolean isFullRoom(final Long id) {
        Room room = findById(id);

        return room.isFull();
    }

    public Room findById(final Long id) {
        return roomRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%d]는 존재하지 않는 방 ID 입니다.", id)));
    }
}
