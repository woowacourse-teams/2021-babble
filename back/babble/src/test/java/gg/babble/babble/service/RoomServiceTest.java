package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.RoomRequest;
import gg.babble.babble.dto.request.TagRequest;
import gg.babble.babble.dto.request.UserJoinRequest;
import gg.babble.babble.dto.response.CreatedRoomResponse;
import gg.babble.babble.dto.response.FoundRoomResponse;
import gg.babble.babble.dto.response.GameResponse;
import gg.babble.babble.dto.response.HeadCountResponse;
import gg.babble.babble.dto.response.TagResponse;
import gg.babble.babble.dto.response.UserResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class RoomServiceTest extends ApplicationTest {

    private static final String LEAGUE_OF_LEGENDS = "League Of Legends";
    private static final String 실버 = "실버";
    private static final String _2시간 = "2시간";
    private static final String 루트 = "루트";
    private static final int COUNT_OF_ONE_PAGE = 16;

    @Autowired
    private RoomService roomService;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    private CreatedRoomResponse savedRoom;

    @BeforeEach
    void setUp() {
        savedRoom = roomService.create(
            new RoomRequest(1L,
                Arrays.asList(
                    new TagRequest(tagService.findByName(실버).get(0).getId()),
                    new TagRequest(tagService.findByName(_2시간).get(0).getId())
                ),
                4
            )
        );

        roomService.sendJoinRoom(savedRoom.getRoomId(), new UserJoinRequest(1L, "1234"));
    }

    @DisplayName("요청한 Id의 방 정보를 반환한다.")
    @Test
    void findTest() {

        Game game = gameService.findByName(LEAGUE_OF_LEGENDS).get(0);
        User user = userService.findByNickname(루트).get(0);
        List<Tag> tags = Arrays.asList(tagService.findByName(실버).get(0),
            tagService.findByName(_2시간).get(0));
        FoundRoomResponse expected = FoundRoomResponse.builder()
            .roomId(savedRoom.getRoomId())
            .game(new GameResponse(game.getId(), game.getName()))
            .host(UserResponse.from(user))
            .headCount(new HeadCountResponse(1, savedRoom.getMaxHeadCount()))
            .tags(tagResponsesFromTags(tags))
            .build();

        FoundRoomResponse foundRoomResponse = roomService.findById(savedRoom.getRoomId());
        assertThat(foundRoomResponse).usingRecursiveComparison()
            .ignoringFields("rooms", "createdDate").isEqualTo(expected);
    }

    private List<TagResponse> tagResponsesFromTags(final List<Tag> tags) {
        return tags.stream()
            .map(TagResponse::from)
            .collect(Collectors.toList());
    }

    @DisplayName("방 Id가 없을 경우 예외를 던진다.")
    @Test
    void roomNotFoundTest() {
        assertThatThrownBy(() -> roomService.findById(Long.MAX_VALUE))
            .isInstanceOf(BabbleNotFoundException.class);
    }

    @DisplayName("요청한 gameId와 page에 해당되는 방 정보들을 반환한다.")
    @Test
    void findRoomsByGameIdAndPageTest() {
        // given
        Long gameId = 1L;
        Pageable pageable = PageRequest.of(0, COUNT_OF_ONE_PAGE);

        // when
        List<FoundRoomResponse> roomResponses = roomService.findGamesByGameIdAndTagIds(gameId, new ArrayList<>(), pageable);

        // then
        assertThat(roomResponses).hasSize(COUNT_OF_ONE_PAGE);
    }

    @DisplayName("요청한 gameId와 page, tags에 해당되는 방 정보들을 반환한다.")
    @Test
    void findRoomsByGameIdAndTagsTest() {
        // given
        Long gameId = 1L;
        Pageable pageable = PageRequest.of(0, COUNT_OF_ONE_PAGE);
        List<Long> tagIds = Arrays.asList(1L, 2L);

        // when
        List<FoundRoomResponse> roomResponses = roomService.findGamesByGameIdAndTagIds(gameId, tagIds, pageable);

        // then
        assertThat(roomResponses).hasSize(COUNT_OF_ONE_PAGE);
    }
}
