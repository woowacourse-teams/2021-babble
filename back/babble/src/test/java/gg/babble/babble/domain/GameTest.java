package gg.babble.babble.domain;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GameTest {

    @DisplayName("게임에 참여 중인 유저 수 반환")
    @Test
    void headCount() {
        // given
        User host = new User(1L, "방장");
        Game game = new Game(1L, "게임 이름", "게임 이미지");
        List<Tag> tags = Arrays.asList(new Tag("실버"), new Tag("2시간"));
        Room room = new Room(1L, game, tags, new MaxHeadCount(4));
        Session session = new Session(1L, "1111", host, room);

        // when
        int count = game.userHeadCount();

        // then
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("게임 정보 편집")
    @Test
    void updateGame() {
        // given
        Game game = new Game(1L, "오래된 게임", "오래된 이미지");
        Game target = new Game("새로운 게임", "새로운 이미지");

        // when
        game.update(target);

        // then
        assertThat(game.getId()).isEqualTo(1L);
        assertThat(game.getName()).isEqualTo(target.getName());
        assertThat(game.getImage()).isEqualTo(target.getImage());
    }

    @DisplayName("게임 삭제")
    @Test
    void deleteGame() {
        // given
        Game game = new Game(1L, "게임 이름", "게임 이미지");

        // when
        game.delete();

        // then
        assertThat(game.isDeleted()).isTrue();
    }
}
