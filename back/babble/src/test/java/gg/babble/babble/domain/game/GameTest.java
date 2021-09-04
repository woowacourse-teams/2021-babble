package gg.babble.babble.domain.game;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.game.AlternativeGameName;
import gg.babble.babble.domain.game.AlternativeGameNames;
import gg.babble.babble.domain.game.Game;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import java.util.Arrays;
import java.util.Collections;
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
        final Game game = new Game(1L, "오래된 게임", "오래된 이미지");
        final Game target = new Game("새로운 게임", "새로운 이미지");

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
        final Game game = new Game(1L, "게임 이름", "게임 이미지");

        // when
        game.delete();

        // then
        assertThat(game.isDeleted()).isTrue();
    }

    @DisplayName("대체 이름 추가")
    @Test
    void alternativeNames() {
        // given
        final Game game = new Game(1L, "오래된 게임", "오래된 이미지");

        // when
        final AlternativeGameName alternativeGameName = new AlternativeGameName("망겜", game);

        // then
        assertThat(game.getAlternativeGameNames()).isEqualTo(new AlternativeGameNames(Collections.singleton(alternativeGameName)));
    }

    @DisplayName("대체 이름 변경")
    @Test
    void changeAlternativeGameName() {
        // given
        Game game = new Game(1L, "오래된 게임", "오래된 이미지");
        Game game2 = new Game(2L, "최신 게임", "최신 이미지");
        // when
        final AlternativeGameName alternativeGameName = new AlternativeGameName("흥겜", game);
        game2.addAlternativeName(alternativeGameName);
        // then
        assertThat(game.hasName(alternativeGameName.getValue())).isFalse();
        assertThat(game2.hasName(alternativeGameName.getValue())).isTrue();
        assertThat(alternativeGameName.getGame()).isEqualTo(game2);
        assertThat(alternativeGameName.isDeleted()).isFalse();
    }

    @DisplayName("대체 이름 삭제")
    @Test
    void removeAlternativeGameName() {
        // given
        final Game game = new Game(1L, "오래된 게임", "오래된 이미지");
        final AlternativeGameName alternativeGameName = new AlternativeGameName("흥겜", game);
        // when
        game.removeAlternativeName(alternativeGameName);
        // then
        assertThat(game.hasName(alternativeGameName.getValue())).isFalse();
        assertThat(alternativeGameName.isDeleted()).isTrue();
    }
}
