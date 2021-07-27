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
        List<Tag> tags = Arrays.asList(tagService.findById(실버),
            tagService.findById(_2시간));
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
            .map(tag -> new TagResponse(tag.getName()))
            .collect(Collectors.toList());
    }

    @DisplayName("방 Id가 없을 경우 예외를 던진다.")
    @Test
    void roomNotFoundTest() {
        assertThatThrownBy(() -> roomService.findById(Long.MAX_VALUE))
            .isInstanceOf(BabbleNotFoundException.class);
    }
}
