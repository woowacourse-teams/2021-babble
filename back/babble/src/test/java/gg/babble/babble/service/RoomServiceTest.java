package gg.babble.babble.service;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoomServiceTest extends ApplicationTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

//    @DisplayName("요청한 Id의 방 정보를 반환한다.")
//    @Transactional
//    @Test
//    void findTest() {
//
//        Game game = gameService.findById(1L);
//        List<Tag> tags = Arrays.asList(tagService.findById("실버"),
//                tagService.findById("2시간"));
//        RoomResponse expected = RoomResponse.from(Room.builder()
//                .id(1L)
//                .createdDate(LocalDateTime.now())
//                .game(game)
//                .tags(tags)
//                .build()
//        );
//
//        RoomResponse roomResponse = roomService.findById(1L);
//        assertThat(roomResponse).usingRecursiveComparison()
//                .ignoringFields("rooms", "createdDate").isEqualTo(expected);
//    }

    @DisplayName("방 Id가 없을 경우 예외를 던진다.")
    @Test
    void roomNotFoundTest() {
        assertThatThrownBy(() -> roomService.findById(Long.MAX_VALUE))
                .isInstanceOf(BabbleNotFoundException.class);
    }
}
