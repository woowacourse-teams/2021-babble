package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GamesTest {

    @Test
    @DisplayName("현재 게임에 참여중인 유저수에 따라 내림차순 정렬을 진행한다.")
    void sortedListByHeadCount() {
        // given
        User 루트 = new User(1L, "루트");
        User 와일더 = new User(2L, "와일더");
        User 피터 = new User(3L, "피터");
        User 그루밍 = new User(4L, "그루밍");

        Game 너구리게임 = new Game(1L, "너구리게임");
        Room 너구리게임_방 = 게임에_해당하는_방을_생성한다(너구리게임);
        게임_방에_유저를_입장_시킨다(너구리게임_방, 루트);
        게임_방에_유저를_입장_시킨다(너구리게임_방, 와일더);
        게임_방에_유저를_입장_시킨다(너구리게임_방, 그루밍);

        Game 피카츄배구 = new Game(2L, "피카츄배구");
        Room 피카츄배구_방 = 게임에_해당하는_방을_생성한다(피카츄배구);
        게임_방에_유저를_입장_시킨다(피카츄배구_방, 피터);

        Games games = new Games(Arrays.asList(너구리게임, 피카츄배구));

        // when
        games.sortedByHeadCount();

        // then
        assertThat(games.toList()).containsExactly(너구리게임, 피카츄배구);
    }

    private void 게임_방에_유저를_입장_시킨다(Room room, User user) {
        room.join(user);
    }

    private Room 게임에_해당하는_방을_생성한다(final Game game) {
        List<Tag> tags = Collections.singletonList(new Tag("good-tag"));
        MaxHeadCount maxHeadCount = new MaxHeadCount(10);
        return new Room(1L, game, tags, maxHeadCount);
    }
}