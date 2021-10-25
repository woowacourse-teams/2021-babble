package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.Session;
import gg.babble.babble.domain.game.AlternativeGameName;
import gg.babble.babble.domain.game.Game;
import gg.babble.babble.domain.room.MaxHeadCount;
import gg.babble.babble.domain.room.Room;
import gg.babble.babble.domain.tag.Tag;
import gg.babble.babble.domain.user.User;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
public class GameRepositoryTest {

    @Autowired
    private AlternativeGameNameRepository alternativeGameNameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GameRepository gameRepository;

    @DisplayName("게임 추가를 수행한다.")
    @Test
    void saveGame() {
        // given
        Game game = new Game("게임 이름", Collections.singletonList("게임 이미지"));
        game.addNames(Collections.singletonList("새 게임"));

        // when
        Game savedGame = gameRepository.save(game);

        // then
        assertThat(savedGame.getId()).isNotNull();
        assertThat(savedGame.getName()).isEqualTo(game.getName());
        assertThat(savedGame.getImages()).isEqualTo(game.getImages());
        assertThat(savedGame.getAlternativeNames()).hasSize(1).contains("새 게임");
    }

    @DisplayName("게임 정보 편집을 수행한다.")
    @Test
    void updateGame() {
        // given
        Game game = gameRepository.save(new Game("오래된 게임", Collections.singletonList("오래된 이미지")));
        Game target = new Game("새로운 게임", Collections.singletonList("오래된 이미지"));

        // when
        game.update(target.getName(), new ArrayList<>(), target.getImages());
        Game foundGame = gameRepository.findById(game.getId())
            .orElseThrow(BabbleNotFoundException::new);

        // then
        assertThat(game).usingRecursiveComparison()
            .isEqualTo(foundGame);
    }

    @DisplayName("사람이 많은 순으로 조회한다.")
    @Test
    void findAllByName() {
        Game game1 = gameRepository.save(new Game("1등 게임"));
        Game game2 = gameRepository.save(new Game("2등 게임"));
        Game game3 = gameRepository.save(new Game("3등 게임"));
        gameRepository.flush();

        Tag tag = tagRepository.save(new Tag("태그"));
        tagRepository.flush();

        Room roomOfGame1 = roomRepository.save(new Room(game1, Collections.singletonList(tag), new MaxHeadCount(10)));
        Room roomOfGame2 = roomRepository.save(new Room(game2, Collections.singletonList(tag), new MaxHeadCount(10)));
        roomRepository.flush();

        User user1 = userRepository.save(new User("user1"));
        User user2 = userRepository.save(new User("user2"));
        User user3 = userRepository.save(new User("user3"));
        userRepository.flush();

        sessionRepository.save(new Session("abc", user1, roomOfGame1));
        sessionRepository.save(new Session("def", user2, roomOfGame1));
        sessionRepository.save(new Session("ghi", user3, roomOfGame2));
        sessionRepository.flush();

        // when
        List<Game> games = gameRepository.findAllByKeyword("게임", PageRequest.of(0, 16));

        // then
        assertThat(games).containsExactly(game1, game2, game3);
    }

    @DisplayName("이름과 대체 이름에 해당 이름을 포함하고 있으면 모두 검색할 수 있다.")
    @Test
    void findAllByNameWithNameAndAlternativeName() {
        // given
        Game game1 = gameRepository.save(new Game("히오스"));
        Game game2 = gameRepository.save(new Game("응원단"));

        alternativeGameNameRepository.save(new AlternativeGameName("오스!", game2));

        List<Game> games = gameRepository.findAllByKeyword("오스", PageRequest.of(0, 16));

        assertThat(games).hasSize(2).contains(game1, game2);
    }

    @DisplayName("페이지네이션 적용을 한다.")
    @Test
    void findAllByNameWithPagination() {
        // given
        for (int i = 0; i < 20; i++) {
            gameRepository.save(new Game("game" + i));
        }
        // when
        List<Game> page0 = gameRepository.findAllByKeyword("", PageRequest.of(0, 16));
        List<Game> page1 = gameRepository.findAllByKeyword("", PageRequest.of(1, 16));

        assertThat(page0).hasSize(16);
        assertThat(page1).hasSize(4);
    }
}
