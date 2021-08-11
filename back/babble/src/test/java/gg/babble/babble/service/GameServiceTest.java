package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.repository.SessionRepository;
import gg.babble.babble.domain.repository.TagRepository;
import gg.babble.babble.domain.repository.UserRepository;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.GameRequest;
import gg.babble.babble.dto.request.SessionRequest;
import gg.babble.babble.dto.response.GameImageResponse;
import gg.babble.babble.dto.response.GameWithImageResponse;
import gg.babble.babble.dto.response.IndexPageGameResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GameServiceTest extends ApplicationTest {

    private static final String DEFAULT_THUMBNAIL = "https://static-cdn.jtvnw.net/ttv-static/404_boxart-1080x1436.jpg";
    private static final String LOL_THUMBNAIL = "LOLOLOL";
    private static final String LOL_NAME = "LEAGUE_OF_LEGENDS";
    private static final String OVERWATCH_NAME = "OVERWATCH";
    private static final String APEX_LEGEND_NAME = "APEX_LEGEND";

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private EnterExitService enterExitService;

    private void prepareDummyGames() {
        gameRepository.save(new Game(LOL_NAME, LOL_THUMBNAIL));
        gameRepository.save(new Game(OVERWATCH_NAME));
        gameRepository.save(new Game(APEX_LEGEND_NAME));
    }

    @DisplayName("게임 Id 조회에 실패할 경우 예외를 던진다.")
    @Test
    void gameNotFoundTest() {
        assertThatThrownBy(() -> gameService.findGameById(Long.MAX_VALUE))
            .isInstanceOf(BabbleNotFoundException.class);
    }

    @DisplayName("게임에 해당하는 이미지를 반환한다.")
    @Test
    void findGameImageById() {
        // given
        prepareDummyGames();

        // given
        GameImageResponse expectedResponse = new GameImageResponse(1L, LOL_THUMBNAIL);

        // then
        assertThat(gameService.findGameImageById(1L)).usingRecursiveComparison()
            .isEqualTo(expectedResponse);
    }

    @DisplayName("전체 게임 이미지 목록을 반환한다.")
    @Test
    void gameImages() {
        prepareDummyGames();

        // given
        List<GameImageResponse> expectedResponses = Arrays.asList(
            new GameImageResponse(1L, LOL_THUMBNAIL),
            new GameImageResponse(2L, DEFAULT_THUMBNAIL),
            new GameImageResponse(3L, DEFAULT_THUMBNAIL)
        );

        // then
        assertThat(gameService.findAllGameImages()).usingRecursiveComparison()
            .isEqualTo(expectedResponses);
    }

    @DisplayName("참가 유저수에 따라 정렬된 게임 리스트를 반환한다.")
    @Test
    void findAllGames() {
        prepareDummyGames();

        Game game = gameRepository.findByIdAndDeletedFalse(3L).orElseThrow(BabbleNotFoundException::new);
        Tag tag = tagRepository.save(new Tag("2시간"));
        Room room = roomRepository.save(new Room(game, Collections.singletonList(tag), new MaxHeadCount(5)));
        User 루트 = userRepository.save(new User("루트"));
        User 와일더 = userRepository.save(new User("와일더"));

        enterExitService.enter(room.getId(), new SessionRequest(루트.getId(), "1111"));
        enterExitService.enter(room.getId(), new SessionRequest(와일더.getId(), "2222"));

        // when
        List<IndexPageGameResponse> expectedResponses = Arrays.asList(
            new IndexPageGameResponse(3L, APEX_LEGEND_NAME, 2, DEFAULT_THUMBNAIL),
            new IndexPageGameResponse(1L, LOL_NAME, 0, LOL_THUMBNAIL),
            new IndexPageGameResponse(2L, OVERWATCH_NAME, 0, DEFAULT_THUMBNAIL)
        );

        List<IndexPageGameResponse> sortedGames = gameService.findSortedGames();

        // then
        assertThat(sortedGames).usingRecursiveComparison()
            .isEqualTo(expectedResponses);
    }

    @DisplayName("ID에 해당되는 단일 게임을 반환한다.")
    @Test
    void findGame() {
        prepareDummyGames();

        // when
        GameWithImageResponse expectedResponse = new GameWithImageResponse(1L, LOL_NAME, LOL_THUMBNAIL);
        // then
        assertThat(gameService.findGame(1L)).usingRecursiveComparison()
            .isEqualTo(expectedResponse);
    }

    @DisplayName("단일 게임을 추가한다.")
    @Test
    void insertGame() {
        // given
        GameRequest request = new GameRequest("너구리 게임", "image.png");

        // when
        GameWithImageResponse response = gameService.insertGame(request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getThumbnail()).isEqualTo(request.getThumbnail());
    }

    @DisplayName("단일 게임 정보를 편집한다.")
    @Test
    void updateGame() {
        // given
        GameWithImageResponse insertGameResponse = gameService.insertGame(new GameRequest("너구리 게임", "썸네일"));
        GameRequest updateRequest = new GameRequest("너구리 게임 - 너굴맨!", "썸네일");

        // when
        GameWithImageResponse updateGameResponse = gameService.updateGame(insertGameResponse.getId(), updateRequest);

        // then
        assertThat(updateGameResponse.getId()).isEqualTo(insertGameResponse.getId());
        assertThat(updateGameResponse.getName()).isEqualTo(updateRequest.getName());
        assertThat(updateGameResponse.getThumbnail()).isEqualTo(updateRequest.getThumbnail());
    }

    @DisplayName("단일 게임을 삭제한다.")
    @Test
    void deleteGame() {
        // given
        GameWithImageResponse insertGameResponse = gameService.insertGame(new GameRequest("너구리 게임", "썸네일"));

        // when
        gameService.deleteGame(insertGameResponse.getId());

        // then
        assertThatThrownBy(() -> gameService.findGame(insertGameResponse.getId()))
            .isInstanceOf(BabbleNotFoundException.class);
    }
}
