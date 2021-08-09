package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SessionTest {

    @DisplayName("세션 삭제")
    @Test
    void deleteSession() {
        // given
        Game game = new Game(1L, "게임 이름", "게임 이미지");
        List<Tag> tags = Collections.singletonList(new Tag(1L, "초보만"));
        MaxHeadCount maxHeadCount = new MaxHeadCount(4);
        Room room = new Room(1L, game, tags, maxHeadCount);
        User user = new User(1L, "코 파는 알리스타", room);
        Session session = new Session(1L, "1A2B3C4D", room, user);

        // when
        session.delete();

        // then
        assertThat(session.isDeleted()).isTrue();
    }
}