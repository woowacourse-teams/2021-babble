package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.repository.SessionRepository;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.dto.request.RoomRequest;
import gg.babble.babble.dto.request.TagRequest;
import gg.babble.babble.dto.response.CreatedRoomResponse;
import gg.babble.babble.dto.response.FoundRoomResponse;
import gg.babble.babble.dto.response.TagResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class RoomServiceTest extends ApplicationTest {

    private static final int COUNT_OF_PAGE = 16;
    @Autowired
    private RoomService roomService;

    @Autowired
    private SessionRepository sessionRepository;

    @DisplayName("방을 생성한다")
    @Test
    void createRoom() {
        // given
        String gameName = "너구리게임";
        String tagName1 = "2시간";
        String tagName2 = "실버";
        Game game = gameRepository.save(new Game(gameName));
        Tag tag1 = tagRepository.save(new Tag(tagName1));
        Tag tag2 = tagRepository.save(new Tag(tagName2));
        TagRequest tagRequest1 = new TagRequest(tag1.getId());
        TagRequest tagRequest2 = new TagRequest(tag2.getId());
        int maxHeadCount = 4;

        RoomRequest roomRequest = new RoomRequest(game.getId(), Arrays.asList(tagRequest1, tagRequest2), maxHeadCount);

        // when
        CreatedRoomResponse response = roomService.create(roomRequest);

        // then
        assertThat(response.getRoomId()).isNotNull();
        assertThat(response.getGame().getName()).isEqualTo(gameName);
        assertThat(response.getCreatedDate()).isNotNull();
        assertThat(response.getMaxHeadCount()).isEqualTo(maxHeadCount);
        assertThat(response.getTags().stream().map(TagResponse::getName)).containsExactly(tagName1, tagName2);
    }

    @DisplayName("요청한 Id의 방 정보를 반환한다.")
    @Test
    void findTest() {
        // given
        String gameName = "너구리게임";
        String tagName1 = "2시간";
        String tagName2 = "실버";
        int maxHeadCount = 4;

        Game game = gameRepository.save(new Game(gameName));
        Tag tag1 = tagRepository.save(new Tag(tagName1));
        Tag tag2 = tagRepository.save(new Tag(tagName2));
        User user = userRepository.save(new User("코 파는 알리스타"));
        Room room = roomRepository.save(new Room(game, Arrays.asList(tag1, tag2), new MaxHeadCount(maxHeadCount)));

        Session session = new Session("11112222", user, room);
        sessionRepository.save(session);

        // when
        FoundRoomResponse roomResponse = roomService.findRoomById(room.getId());

        // then
        assertThat(roomResponse.getRoomId()).isEqualTo(room.getId());
        assertThat(roomResponse.getGame()).usingRecursiveComparison().isEqualTo(room.getGame());
        assertThat(roomResponse.getHost()).usingRecursiveComparison().isEqualTo(user);
        assertThat(roomResponse.getHeadCount().getCurrent()).isEqualTo(1);
        assertThat(roomResponse.getHeadCount().getMax()).isEqualTo(maxHeadCount);
        assertThat(roomResponse.getTags().stream().map(TagResponse::getName)).containsExactly(tagName1, tagName2);
    }

    @DisplayName("방 Id가 없을 경우 예외를 던진다.")
    @Test
    void roomNotFoundTest() {
        assertThatThrownBy(() -> roomService.findRoomById(Long.MAX_VALUE))
            .isInstanceOf(BabbleNotFoundException.class);
    }

    @DisplayName("방에 남은 유저가 없을 경우 방이 자동으로 삭제된다.")
    @Test
    void autoDeleteRoom() {
        String gameName = "너구리게임";
        String tagName1 = "2시간";
        String tagName2 = "실버";
        int maxHeadCount = 4;

        Game game = gameRepository.save(new Game(gameName));
        Tag tag1 = tagRepository.save(new Tag(tagName1));
        Tag tag2 = tagRepository.save(new Tag(tagName2));
        User user = userRepository.save(new User("코 파는 알리스타"));
        Room room = roomRepository.save(new Room(game, Arrays.asList(tag1, tag2), new MaxHeadCount(maxHeadCount)));

        Session session = new Session("11112222", user, room);
        sessionRepository.save(session);

        assertThat(roomService.findRoomById(room.getId())).isInstanceOf(FoundRoomResponse.class);

        session.delete();

        assertThatThrownBy(() -> roomService.findRoomById(room.getId()))
            .isInstanceOf(BabbleNotFoundException.class);
    }

    @DisplayName("요청한 gameId와 page에 해당되는 방 정보들을 반환한다.")
    @Test
    void findRoomsByGameIdAndPageTest() {
        // given
        Game game = gameRepository.save(new Game("너구리게임"));
        Tag tag1 = tagRepository.save(new Tag("2시간"));
        Tag tag2 = tagRepository.save(new Tag("초보만"));
        방_20개_생성(game, Arrays.asList(tag1, tag2));

        Pageable pageable = PageRequest.of(0, COUNT_OF_PAGE);

        // when
        List<FoundRoomResponse> roomResponses = roomService.findGamesByGameIdAndTagIds(game.getId(), new ArrayList<>(), pageable);

        // then
        assertThat(roomResponses).hasSize(16);
    }

    @DisplayName("요청한 gameId와 page, tags에 해당되는 방 정보들을 반환한다.")
    @Test
    void findRoomsByGameIdAndTagsTest() {
        // given
        Game game = gameRepository.save(new Game("너구리게임"));
        Tag tag1 = tagRepository.save(new Tag("2시간"));
        Tag tag2 = tagRepository.save(new Tag("초보만"));
        방_20개_생성(game, Arrays.asList(tag1, tag2));

        Pageable pageable = PageRequest.of(0, COUNT_OF_PAGE);
        List<Long> tagIds = Arrays.asList(tag1.getId(), tag2.getId());

        // when
        List<FoundRoomResponse> roomResponses = roomService.findGamesByGameIdAndTagIds(game.getId(), tagIds, pageable);

        // then
        assertThat(roomResponses).hasSize(16);
    }

    private void 방_20개_생성(final Game game, final List<Tag> tags) {
        for (int i = 0; i < 20; i++) {
            User user = userRepository.save(new User(Integer.toString(i)));
            Room room = roomRepository.save(new Room(game, tags, new MaxHeadCount(5)));

            sessionRepository.save(new Session(Integer.toString(i), user, room));
        }
    }
}
