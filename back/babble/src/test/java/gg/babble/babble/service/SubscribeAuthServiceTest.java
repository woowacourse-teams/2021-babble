package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.Game;
import gg.babble.babble.domain.repository.GameRepository;
import gg.babble.babble.domain.repository.RoomRepository;
import gg.babble.babble.domain.repository.TagRepository;
import gg.babble.babble.domain.repository.UserRepository;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.Nickname;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleIllegalStatementException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscribeAuthServiceTest extends ApplicationTest {

    private static final String APEX_LEGEND = "Apex Legend";
    private static final String 현구막 = "현구막";
    private static final String 포츈 = "포츈";
    private static final String 실버 = "실버";

    @Autowired
    private SubscribeAuthService subscribeAuthService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private RoomRepository roomRepository;

    @DisplayName("새로운 유저가 방에 들어오더라도, 정원초과가 발생하지 않으면 예외가 발생하지 않는다.")
    @Test
    void userJoinTest() {
        prepareDummyRoom();
        assertThatCode(() -> {
            subscribeAuthService.validate("/topic/rooms/2/users");
        }).doesNotThrowAnyException();
    }


    @DisplayName("방 인원수보다 더 많은 인원이 입장하려고 하는경우, 예외가 발생한다.")
    @Test
    void overcrowdExceptionTest() {
        Room room = prepareDummyRoom();
        User guest = userRepository.findByNickname(new Nickname(포츈)).get(0);

        room.join(guest);
        roomRepository.save(room);

        assertThatThrownBy(() -> {
            subscribeAuthService.validate(String.format("/topic/rooms/%s/users", room.getId()));
        }).isInstanceOf(BabbleIllegalStatementException.class);
    }

    private Room prepareDummyRoom() {
        Game game = gameRepository.findByNameAndDeletedFalse(APEX_LEGEND).get(0);
        User host = userRepository.findByNickname(new Nickname(현구막)).get(0);
        List<Tag> tags = Collections.singletonList(tagRepository.findByName(실버).get(0));

        Room room = new Room(game, tags, new MaxHeadCount(2));

        room.join(host);
        return roomRepository.save(room);
    }
}
