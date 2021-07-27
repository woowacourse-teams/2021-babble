package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.dto.GameResponse;
import gg.babble.babble.dto.RoomResponse;
import gg.babble.babble.dto.TagResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RoomServiceTest extends ApplicationTest {

    private static final String LEAGUE_OF_LEGEND = "League Of Legend";
    private static final String 실버 = "실버";
    private static final String _2시간 = "2시간";

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

        Game game = gameService.findByName(LEAGUE_OF_LEGEND).get(0);
        List<Tag> tags = Arrays.asList(tagService.findById(실버),
            tagService.findById(_2시간));
        RoomResponse expected = RoomResponse.builder()
            .roomId(1L)
            .game(new GameResponse(game.getId(), game.getName()))
            .tags(tagResponsesFromTags(tags))
            .build();

        RoomResponse roomResponse = roomService.findById(1L);
        assertThat(roomResponse).usingRecursiveComparison()
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
