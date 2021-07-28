package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.FoundRoomResponse;
import gg.babble.babble.dto.GameResponse;
import gg.babble.babble.dto.HeadCountResponse;
import gg.babble.babble.dto.TagResponse;
import gg.babble.babble.dto.UserResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoomServiceTest extends ApplicationTest {

    private static final String LEAGUE_OF_LEGENDS = "League Of Legends";
    private static final String 실버 = "실버";
    private static final String _2시간 = "2시간";
    private static final String 루트 = "루트";

    @Autowired
    private RoomService roomService;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @DisplayName("요청한 Id의 방 정보를 반환한다.")
    @Test
    void findTest() {

        Game game = gameService.findByName(LEAGUE_OF_LEGENDS).get(0);
        User user = userService.findByNickname(루트).get(0);
        List<Tag> tags = Arrays.asList(tagService.findByName(실버).get(0),
            tagService.findByName(_2시간).get(0));
        FoundRoomResponse expected = FoundRoomResponse.builder()
            .roomId(1L)
            .game(new GameResponse(game.getId(), game.getName()))
            .host(UserResponse.from(user))
            .headCount(new HeadCountResponse(1, 4))
            .tags(tagResponsesFromTags(tags))
            .build();

        FoundRoomResponse foundRoomResponse = roomService.findById(1L);
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

    @DisplayName("요청한 gameId에 해당되는 방 정보들을 반환한다.")
    @Test
    void findRoomsByGameIdTest() {
        // given
        int gameId = 1;
        // 이런거 테스트 충족을 위해서 데이터로더가 아니고 독립적인 테스트 단위로 데이터를 채워넣어야한다고 생각되긴 함..
        int countOfOnePage = 16;

        // when
        List<FoundRoomResponse> roomResponses = roomService.findGamesByGameId(gameId);

        // then
        assertThat(roomResponses).hasSize(countOfOnePage);
    }

    @DisplayName("요청한 gameId와 page에 해당되는 방 정보들을 반환한다.")
    @Test
    void findRoomsByGameIdAndPageTest() {
        // given
        int gameId = 1;
        int page = 1;
        // 이런거 테스트 충족을 위해서 데이터로더가 아니고 독립적인 테스트 단위로 데이터를 채워넣어야한다고 생각되긴 함..
        int countOfOnePage = 16;

        // when
        List<FoundRoomResponse> roomResponses = roomService.findGamesByGameId(gameId, page);

        // then
        assertThat(roomResponses).hasSize(countOfOnePage);
    }

    @DisplayName("요청한 gameId와 tags에 해당되는 방 정보들을 반환한다.")
    @Test
    void findRoomsByGameIdAndTagsTest() {
        // given
        int gameId = 1;
        List<Long> tagIds = Arrays.asList(1L, 2L, 3L);
        // 이런거 테스트 충족을 위해서 데이터로더가 아니고 독립적인 테스트 단위로 데이터를 채워넣어야한다고 생각되긴 함..
        int countOfOnePage = 16;

        // when
        List<FoundRoomResponse> roomResponses = roomService.findGamesByGameId(gameId, tagIds);

        // then
        assertThat(roomResponses).hasSize(countOfOnePage);
    }

    @DisplayName("요청한 gameId와 page, tags에 해당되는 방 정보들을 반환한다.")
    @Test
    void findRoomsByGameIdAndTagsTest() {
        // given
        int gameId = 1;
        int page = 1;
        List<Long> tagIds = Arrays.asList(1L, 2L, 3L);
        // 이런거 테스트 충족을 위해서 데이터로더가 아니고 독립적인 테스트 단위로 데이터를 채워넣어야한다고 생각되긴 함..
        int countOfOnePage = 16;

        // when
        List<FoundRoomResponse> roomResponses = roomService.findGamesByGameId(gameId, tagIds, page);

        // then
        assertThat(roomResponses).hasSize(countOfOnePage);
    }
}
