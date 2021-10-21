package gg.babble.babble.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gg.babble.babble.domain.board.Board;
import gg.babble.babble.exception.BabbleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    private Board post1;
    private Board post2;
    private Board post3;
    private Board post4;

    @BeforeEach
    public void setUp() {
        post1 = boardRepository.save(new Board("이것은 처음쓴 글",
            "안녕하세요~!!!\n저는 첫 번째 글의 주인공이랍니다.",
            "자유",
            "조용히좋아요를눌러라",
            "123456"));
        post2 = boardRepository.save(new Board("나랑 밥먹을 사람?",
            "하이루 방가링가\n오늘은 중식이 땡기는구만\n어디 좋은 중국집 아는데 있는사람 추천좀 ㅎㅎ",
            "건의",
            "멘보샤",
            "234567"));
        post3 = boardRepository.save(new Board("니들이 게맛을 알아?",
            "간장게장 먹고싶다.\n누가 나 좀 간장게장좀 사줘~",
            "자유",
            "밥도둑",
            "345678"));
        post4 = boardRepository.save(new Board("게임 좀 추가해줘요.",
            "",
            "게임명",
            "게임이좋아",
            "123456"));
    }

    @DisplayName("게시글을 저장한다.")
    @Test
    void save() {
        //given
        Board board = new Board("테스트를 위해 작성된 게시글",
            "테스트가 과연 성공할 수 있을까요?\n이 테스트가 성공한다면 엄청난 일이 일어날지도...?",
            "자유",
            "테스터",
            "123456");

        //when
        Board post = boardRepository.save(board);
        Board expected = new Board("테스트를 위해 작성된 게시글",
            "테스트가 과연 성공할 수 있을까요?\n이 테스트가 성공한다면 엄청난 일이 일어날지도...?",
            "자유",
            "테스터",
            "123456");

        //then
        assertThat(post).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(expected);
    }

    @DisplayName("게시글을 조회한다.")
    @Test
    void find() {
        //when
        Board post = boardRepository.findById(post1.getId()).orElseThrow(BabbleNotFoundException::new);

        //then
        assertThat(post).usingRecursiveComparison().ignoringFields("createdAt", "updatedAt").isEqualTo(post1);
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    void update() {
        //given
        Board board = boardRepository.findById(post2.getId()).orElseThrow(BabbleNotFoundException::new);

        //when
        board.update("이것은 수정된 글",
            "수정된 글이지롱~",
            "건의",
            "234567");
        Board result = boardRepository.findById(post2.getId()).orElseThrow(BabbleNotFoundException::new);

        //then
        assertThat(result).usingRecursiveComparison()
            .ignoringFields("title", "content", "category", "updatedAt").isEqualTo(board);
        assertThat(result.title()).isEqualTo("이것은 수정된 글");
        assertThat(result.content()).isEqualTo("수정된 글이지롱~");
        assertThat(result.category()).isEqualTo("건의");
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void delete() {
        //when
        boardRepository.delete(post1);

        //then
        assertThat(boardRepository.findAll()).hasSize(3);
    }
}
