package gg.babble.babble.domain.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleDuplicatedException;
import java.util.ArrayList;
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
        Game game = new Game(1L, "게임 이름", Collections.singletonList("게임 이미지"));
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
        Game game = new Game(1L, "오래된 게임", Collections.singletonList("오래된 이미지"));
        Game target = new Game("새로운 게임", Collections.singletonList("새로운 이미지"));

        // when
        game.update(target.getName(), new ArrayList<>(), target.getImages());

        // then
        assertThat(game.getId()).isEqualTo(1L);
        assertThat(game.getName()).isEqualTo(target.getName());
        assertThat(game.getImages()).isEqualTo(target.getImages());
    }

    @DisplayName("대체 이름 추가")
    @Test
    void alternativeNames() {
        // given
        Game game = new Game(1L, "오래된 게임", Collections.singletonList("오래된 이미지"));

        // when
        AlternativeGameName alternativeGameName = new AlternativeGameName("망겜", game);
        game.addAlternativeName(alternativeGameName);

        // then
        assertThat(game.getAlternativeGameNames()).isEqualTo(new AlternativeGameNames(Collections.singletonList(alternativeGameName)));
    }

    @DisplayName("대체 이름 변경")
    @Test
    void changeAlternativeGameName() {
        // given
        Game game = new Game(1L, "오래된 게임", Collections.singletonList("오래된 이미지"));
        AlternativeGameName alternativeGameName = new AlternativeGameName(1L, "흥겜", game);
        game.addAlternativeName(alternativeGameName);
        Game target = new Game("새로운 게임", Collections.singletonList("새로운 이미지"));
        AlternativeGameName alternativeTargetGameName = new AlternativeGameName(1L, "망겜", target);
        target.addAlternativeName(alternativeGameName);

        // when
        game.update(target.getName(), target.getAlternativeNames(), target.getImages());

        // then
        assertThat(game.getId()).isEqualTo(1L);
        assertThat(game.getName()).isEqualTo(target.getName());
        assertThat(game.getImages()).isEqualTo(target.getImages());
        assertThat(game.getAlternativeGameNames()).isEqualTo(new AlternativeGameNames(Collections.singletonList(alternativeTargetGameName)));
    }

    @DisplayName("대체 이름 삭제")
    @Test
    void removeAlternativeGameName() {
        // given
        Game game = new Game(1L, "오래된 게임", Collections.singletonList("오래된 이미지"));
        AlternativeGameName alternativeGameName = new AlternativeGameName("흥겜", game);
        game.addAlternativeName(alternativeGameName);

        // when
        game.removeAlternativeName(alternativeGameName);

        // then
        assertThat(game.hasName(alternativeGameName.getValue())).isFalse();
        assertThat(alternativeGameName.isDeleted()).isTrue();
    }

    @DisplayName("복수 개의 이름 추가")
    @Test
    void addNames() {
        // given
        Game game = new Game(1L, "오래된 게임", Collections.singletonList("오래된 이미지"));

        // when
        List<String> alternativeNames = Arrays.asList("망겜", "국민겜", "사골");
        game.addNames(alternativeNames);

        // then
        assertThat(game.getAlternativeNames()).hasSameSizeAs(alternativeNames).containsAll(alternativeNames);
    }

    @DisplayName("복수 개의 이름 추가시 이미 존재하는 이름이면 예외 처리")
    @Test
    void invalidAddNames() {
        // given
        Game game = new Game(1L, "오래된 게임", Collections.singletonList("오래된 이미지"));

        // then
        List<String> alternativeNames = Arrays.asList("망겜", "오래된 게임", "사골");
        assertThatThrownBy(() -> game.addNames(alternativeNames)).isExactlyInstanceOf(BabbleDuplicatedException.class);
    }

    @DisplayName("복수 개의 이름 추가시 중복된 이름이 있으면 예외 처리")
    @Test
    void addDuplicatedNames() {
        // given
        Game game = new Game(1L, "오래된 게임", Collections.singletonList("오래된 이미지"));

        // when
        List<String> alternativeNames = Arrays.asList("망겜", "망겜", "사골");
        assertThatThrownBy(() -> game.addNames(alternativeNames)).isExactlyInstanceOf(BabbleDuplicatedException.class);
    }
}
